package it.unicam.cs.ids.agriplatform.controllers.certification;

import it.unicam.cs.ids.agriplatform.dto.input.certification.CreateCertificationDTO;
import it.unicam.cs.ids.agriplatform.dto.input.certification.UpdateCertificationDTO;
import it.unicam.cs.ids.agriplatform.dto.output.CertificationResponseDTO;
import it.unicam.cs.ids.agriplatform.services.CertificationService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    @Autowired
    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    /**
     * GET /api/certifications - Get all certifications
     */
    @GetMapping
    public ResponseEntity<?> getAllCertifications() {
        List<CertificationResponseDTO> certifications = certificationService.getAllCertifications();
        return ApiResponse.ok("Certifications retrieved successfully", certifications);
    }

    /**
     * GET /api/certifications/approved - Get approved certifications only
     */
    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedCertifications() {
        List<CertificationResponseDTO> certifications = certificationService.getApprovedCertifications();
        return ApiResponse.ok("Approved certifications retrieved successfully", certifications);
    }

    /**
     * GET /api/certifications/pending - Get pending certifications
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingCertifications() {
        List<CertificationResponseDTO> certifications = certificationService.getPendingCertifications();
        return ApiResponse.ok("Pending certifications retrieved successfully", certifications);
    }

    /**
     * GET /api/certifications/{id} - Get certification by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCertificationById(@PathVariable Long id) {
        return certificationService.getCertificationById(id)
                .map(cert -> ApiResponse.ok("Certification retrieved successfully", cert))
                .orElse(ApiResponse.notFound("Certification not found with id: " + id));
    }

    /**
     * GET /api/certifications/search/issuer?q={issuer} - Search by issuer
     */
    @GetMapping("/search/issuer")
    public ResponseEntity<?> searchByIssuer(@RequestParam String q) {
        List<CertificationResponseDTO> certifications = certificationService.searchByIssuer(q);
        return ApiResponse.ok("Certifications found", certifications);
    }

    /**
     * GET /api/certifications/search/name?q={name} - Search by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<?> searchByName(@RequestParam String q) {
        List<CertificationResponseDTO> certifications = certificationService.searchByName(q);
        return ApiResponse.ok("Certifications found", certifications);
    }

    /**
     * POST /api/certifications - Create a new certification
     */
    @PostMapping
    public ResponseEntity<?> createCertification(@Valid @RequestBody CreateCertificationDTO dto) {
        CertificationResponseDTO created = certificationService.createCertification(dto);
        return ApiResponse.created("Certification created successfully", created);
    }

    /**
     * PUT /api/certifications/{id} - Update a certification
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertification(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCertificationDTO dto) {
        return certificationService.updateCertification(id, dto)
                .map(cert -> ApiResponse.ok("Certification updated successfully", cert))
                .orElse(ApiResponse.notFound("Certification not found with id: " + id));
    }

    /**
     * PUT /api/certifications/{id}/approve - Approve a certification (Curator only)
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveCertification(@PathVariable Long id) {
        return certificationService.approveCertification(id)
                .map(cert -> ApiResponse.ok("Certification approved successfully", cert))
                .orElse(ApiResponse.notFound("Certification not found with id: " + id));
    }

    /**
     * PUT /api/certifications/{id}/reject - Reject a certification
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectCertification(@PathVariable Long id) {
        return certificationService.rejectCertification(id)
                .map(cert -> ApiResponse.ok("Certification rejected successfully", cert))
                .orElse(ApiResponse.notFound("Certification not found with id: " + id));
    }

    /**
     * DELETE /api/certifications/{id} - Delete a certification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) {
        boolean deleted = certificationService.deleteCertification(id);
        if (deleted) {
            return ApiResponse.ok("Certification deleted successfully", (Void) null);
        }
        return ApiResponse.notFound("Certification not found with id: " + id);
    }
}
