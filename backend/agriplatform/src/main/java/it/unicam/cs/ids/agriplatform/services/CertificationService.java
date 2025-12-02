package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.certification.CreateCertificationDTO;
import it.unicam.cs.ids.agriplatform.dto.input.certification.UpdateCertificationDTO;
import it.unicam.cs.ids.agriplatform.dto.output.CertificationResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Certification;
import it.unicam.cs.ids.agriplatform.repositories.CertificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;

    @Autowired
    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    /**
     * Get all certifications
     */
    public List<CertificationResponseDTO> getAllCertifications() {
        return certificationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get approved certifications only
     */
    public List<CertificationResponseDTO> getApprovedCertifications() {
        return certificationRepository.findByIsApprovedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending certifications (not approved)
     */
    public List<CertificationResponseDTO> getPendingCertifications() {
        return certificationRepository.findByIsApprovedFalse()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get certification by ID
     */
    public Optional<CertificationResponseDTO> getCertificationById(Long id) {
        return certificationRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Search certifications by issuer
     */
    public List<CertificationResponseDTO> searchByIssuer(String issuer) {
        return certificationRepository.findByIssuerContainingIgnoreCase(issuer)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search certifications by name
     */
    public List<CertificationResponseDTO> searchByName(String name) {
        return certificationRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new certification
     */
    public CertificationResponseDTO createCertification(CreateCertificationDTO dto) {
        Certification certification = new Certification(
                null,
                dto.name(),
                dto.description(),
                dto.issuer(),
                false // New certifications start as not approved
        );

        Certification saved = certificationRepository.save(certification);
        return mapToDTO(saved);
    }

    /**
     * Update an existing certification
     */
    public Optional<CertificationResponseDTO> updateCertification(Long id, UpdateCertificationDTO dto) {
        return certificationRepository.findById(id).map(certification -> {
            certification.setName(dto.name());
            certification.setDescription(dto.description());
            certification.setIssuer(dto.issuer());

            Certification updated = certificationRepository.save(certification);
            return mapToDTO(updated);
        });
    }

    /**
     * Approve a certification (Curator only)
     */
    public Optional<CertificationResponseDTO> approveCertification(Long id) {
        return certificationRepository.findById(id).map(certification -> {
            certification.setApproved(true);
            Certification updated = certificationRepository.save(certification);
            return mapToDTO(updated);
        });
    }

    /**
     * Reject/Unapprove a certification
     */
    public Optional<CertificationResponseDTO> rejectCertification(Long id) {
        return certificationRepository.findById(id).map(certification -> {
            certification.setApproved(false);
            Certification updated = certificationRepository.save(certification);
            return mapToDTO(updated);
        });
    }

    /**
     * Delete a certification
     */
    public boolean deleteCertification(Long id) {
        if (certificationRepository.existsById(id)) {
            certificationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Map Certification entity to DTO
     */
    private CertificationResponseDTO mapToDTO(Certification certification) {
        return new CertificationResponseDTO(
                certification.getId(),
                certification.getName(),
                certification.getDescription(),
                certification.getIssuer(),
                certification.isApproved());
    }
}
