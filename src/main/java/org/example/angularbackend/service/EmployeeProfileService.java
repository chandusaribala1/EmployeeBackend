package org.example.angularbackend.service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import org.example.angularbackend.dto.EmployeeProfileRequest;
import org.example.angularbackend.dto.EmployeeProfileResponse;
import org.example.angularbackend.entity.EmployeeProfile;
import org.example.angularbackend.entity.EmployeeSkill;
import org.example.angularbackend.entity.EmployeeCertification;
import org.example.angularbackend.repository.EmployeeCertificationRepository;
import org.example.angularbackend.repository.EmployeeProfileRepository;
import org.example.angularbackend.repository.EmployeeRepository;
import org.example.angularbackend.repository.EmployeeSkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeProfileService {
    private static final int MAX_IMAGE_BYTES = 3 * 1024 * 1024;
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};

    private final EmployeeRepository employeeRepository;
    private final EmployeeProfileRepository profileRepository;
    private final EmployeeSkillRepository skillRepository;
    private final EmployeeCertificationRepository certificationRepository;

    public EmployeeProfileService(EmployeeRepository employeeRepository, EmployeeProfileRepository profileRepository,
                                  EmployeeSkillRepository skillRepository, EmployeeCertificationRepository certificationRepository) {
        this.employeeRepository = employeeRepository;
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
        this.certificationRepository = certificationRepository;
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse get(String employeeId) {
        String id = normalizeId(employeeId);
        if (!employeeRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Employee was not found.");
        }
        EmployeeProfile profile = profileRepository.findById(id).orElse(null);
        if (profile == null) {
            return emptyProfile(id);
        }
        List<String> skills = skillRepository.findAllByEmployeeIdOrderBySkillNameAsc(id).stream().map(EmployeeSkill::getSkillName).toList();
        List<String> certifications = certificationRepository.findAllByEmployeeIdOrderByCertificationNameAsc(id).stream()
                .map(EmployeeCertification::getCertificationName).toList();
        return toResponse(profile, skills, certifications);
    }

    @Transactional
    public EmployeeProfileResponse save(String employeeId, EmployeeProfileRequest request) {
        String id = normalizeId(employeeId);
        if (!employeeRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Employee was not found.");
        }
        EmployeeProfile profile = profileRepository.findById(id).orElseGet(() -> {
            EmployeeProfile created = new EmployeeProfile();
            created.setEmployeeId(id);
            return created;
        });
        profile.setFatherName(request.fatherName().trim());
        profile.setMotherName(request.motherName().trim());
        profile.setPermanentAddress(request.permanentAddress().trim());
        profile.setDateOfBirth(request.dateOfBirth());
        if (request.profileImageBase64() != null && !request.profileImageBase64().isBlank()) {
            profile.setProfileImage(decodePng(request.profileImageBase64()));
        }
        profile.setTenthMarks(request.tenthMarks());
        profile.setIntermediateType(request.intermediateType());
        profile.setIntermediateMarks(request.intermediateMarks());
        profile.setEducationGroup(request.educationGroup().trim());
        profile.setGraduationQualification(request.graduationQualification().trim());
        profile.setGraduationSpecialization(blankToNull(request.graduationSpecialization()));
        profile.setGraduationGroup(blankToNull(request.graduationGroup()));
        profile.setPostgraduateQualification(blankToNull(request.postgraduateQualification()));
        profile.setPostgraduateSpecialization(blankToNull(request.postgraduateSpecialization()));
        profile.setAadhaarNumber(blankToNull(request.aadhaarNumber()));
        profile.setPanNumber(blankToNull(request.panNumber()) == null ? null : request.panNumber().trim().toUpperCase(Locale.ROOT));
        profile.setUpdatedAt(java.time.Instant.now());
        profileRepository.save(profile);

        skillRepository.deleteForEmployee(id);
        certificationRepository.deleteForEmployee(id);
        List<EmployeeSkill> skills = normalizedDistinct(request.skills()).stream().map(skill -> new EmployeeSkill(id, skill)).toList();
        List<EmployeeCertification> certifications = normalizedDistinct(request.certifications()).stream()
                .map(name -> new EmployeeCertification(id, name)).toList();
        skillRepository.saveAll(skills);
        certificationRepository.saveAll(certifications);
        return toResponse(profile, skills.stream().map(EmployeeSkill::getSkillName).toList(),
                certifications.stream().map(EmployeeCertification::getCertificationName).toList());
    }

    private static byte[] decodePng(String encodedImage) {
        try {
            String value = encodedImage.startsWith("data:image/png;base64,")
                    ? encodedImage.substring("data:image/png;base64,".length()) : encodedImage;
            byte[] image = Base64.getDecoder().decode(value);
            if (image.length > MAX_IMAGE_BYTES || image.length < PNG_SIGNATURE.length
                    || !Arrays.equals(Arrays.copyOf(image, PNG_SIGNATURE.length), PNG_SIGNATURE)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Profile image must be a PNG file no larger than 3 MB.");
            }
            return image;
        } catch (IllegalArgumentException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Profile image is not valid Base64 PNG data.");
        }
    }

    private static List<String> normalizedDistinct(List<String> values) {
        return values.stream().map(String::trim).filter(value -> !value.isEmpty())
                .distinct().toList();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String normalizeId(String value) { return value.trim().toUpperCase(Locale.ROOT); }

    private static EmployeeProfileResponse emptyProfile(String id) {
        return new EmployeeProfileResponse(id, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, List.of(), List.of());
    }

    private static EmployeeProfileResponse toResponse(EmployeeProfile profile, List<String> skills, List<String> certifications) {
        byte[] image = profile.getProfileImage();
        return new EmployeeProfileResponse(profile.getEmployeeId(), profile.getFatherName(), profile.getMotherName(),
                profile.getPermanentAddress(), profile.getDateOfBirth(), image == null ? null : Base64.getEncoder().encodeToString(image),
                profile.getTenthMarks(), profile.getIntermediateType(), profile.getIntermediateMarks(), profile.getEducationGroup(),
                profile.getGraduationQualification(), profile.getGraduationSpecialization(), profile.getGraduationGroup(),
                profile.getPostgraduateQualification(), profile.getPostgraduateSpecialization(), profile.getAadhaarNumber(),
                profile.getPanNumber(), skills, certifications);
    }
}
