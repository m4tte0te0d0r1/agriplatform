package it.unicam.cs.ids.agriplatform.controllers.company;

import it.unicam.cs.ids.agriplatform.dto.input.company.CreateCompanyDTO;
import it.unicam.cs.ids.agriplatform.dto.input.company.UpdateCompanyDTO;
import it.unicam.cs.ids.agriplatform.dto.output.CompanyResponseDTO;
import it.unicam.cs.ids.agriplatform.services.CompanyService;
import it.unicam.cs.ids.agriplatform.utils.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * GET /api/companies - Get all companies
     */
    @GetMapping
    public ResponseEntity<?> getAllCompanies() {
        List<CompanyResponseDTO> companies = companyService.getAllCompanies();
        return ApiResponse.ok("Companies retrieved successfully", companies);
    }

    /**
     * GET /api/companies/{id} - Get company by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(company -> ApiResponse.ok("Company retrieved successfully", company))
                .orElse(ApiResponse.notFound("Company not found with id: " + id));
    }

    /**
     * GET /api/companies/user/{userId} - Get companies by user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCompaniesByUserId(@PathVariable Long userId) {
        List<CompanyResponseDTO> companies = companyService.getCompaniesByUserId(userId);
        return ApiResponse.ok("User companies retrieved successfully", companies);
    }

    /**
     * GET /api/companies/my - Get current user's companies
     */
    @GetMapping("/my")
    public ResponseEntity<?> getCurrentUserCompanies() {
        List<CompanyResponseDTO> companies = companyService.getCurrentUserCompanies();
        return ApiResponse.ok("Your companies retrieved successfully", companies);
    }

    /**
     * GET /api/companies/search/name?q={name} - Search by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<?> searchByName(@RequestParam String q) {
        List<CompanyResponseDTO> companies = companyService.searchByName(q);
        return ApiResponse.ok("Companies found", companies);
    }

    /**
     * GET /api/companies/search/address?q={address} - Search by address
     */
    @GetMapping("/search/address")
    public ResponseEntity<?> searchByAddress(@RequestParam String q) {
        List<CompanyResponseDTO> companies = companyService.searchByAddress(q);
        return ApiResponse.ok("Companies found", companies);
    }

    /**
     * POST /api/companies - Create a new company
     */
    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CreateCompanyDTO dto) {
        CompanyResponseDTO created = companyService.createCompany(dto);
        return ApiResponse.created("Company created successfully", created);
    }

    /**
     * PUT /api/companies/{id} - Update a company
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyDTO dto) {
        try {
            return companyService.updateCompany(id, dto)
                    .map(company -> ApiResponse.ok("Company updated successfully", company))
                    .orElse(ApiResponse.notFound("Company not found with id: " + id));
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * DELETE /api/companies/{id} - Delete a company
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            boolean deleted = companyService.deleteCompany(id);
            if (deleted) {
                return ApiResponse.ok("Company deleted successfully", (Void) null);
            }
            return ApiResponse.notFound("Company not found with id: " + id);
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
}
