package it.unicam.cs.ids.agriplatform.services;

import it.unicam.cs.ids.agriplatform.dto.input.company.CreateCompanyDTO;
import it.unicam.cs.ids.agriplatform.dto.input.company.UpdateCompanyDTO;
import it.unicam.cs.ids.agriplatform.dto.output.CompanyResponseDTO;
import it.unicam.cs.ids.agriplatform.models.Company;
import it.unicam.cs.ids.agriplatform.models.User;
import it.unicam.cs.ids.agriplatform.repositories.CompanyRepository;
import it.unicam.cs.ids.agriplatform.utils.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserContext userContext;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, UserContext userContext) {
        this.companyRepository = companyRepository;
        this.userContext = userContext;
    }

    /**
     * Get all companies
     */
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get company by ID
     */
    public Optional<CompanyResponseDTO> getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(this::mapToDTO);
    }

    /**
     * Get companies by user ID
     */
    public List<CompanyResponseDTO> getCompaniesByUserId(Long userId) {
        return companyRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get current user's companies
     */
    public List<CompanyResponseDTO> getCurrentUserCompanies() {
        User currentUser = userContext.getCurrentUser();
        return getCompaniesByUserId(currentUser.getId());
    }

    /**
     * Search companies by name
     */
    public List<CompanyResponseDTO> searchByName(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search companies by address
     */
    public List<CompanyResponseDTO> searchByAddress(String address) {
        return companyRepository.findByAddressContainingIgnoreCase(address)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new company
     */
    public CompanyResponseDTO createCompany(CreateCompanyDTO dto) {
        User currentUser = userContext.getCurrentUser();

        Company company = new Company(
                0L,
                dto.name(),
                dto.address(),
                currentUser.getId(),
                dto.latitude(),
                dto.longitude());

        Company saved = companyRepository.save(company);
        return mapToDTO(saved);
    }

    /**
     * Update an existing company
     */
    public Optional<CompanyResponseDTO> updateCompany(Long id, UpdateCompanyDTO dto) {
        User currentUser = userContext.getCurrentUser();

        return companyRepository.findById(id).map(company -> {
            // Check if user owns the company
            if (company.getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only update your own companies");
            }

            company.setName(dto.name());
            company.setAddress(dto.address());
            company.setLatitude(dto.latitude());
            company.setLongitude(dto.longitude());

            Company updated = companyRepository.save(company);
            return mapToDTO(updated);
        });
    }

    /**
     * Delete a company
     */
    public boolean deleteCompany(Long id) {
        User currentUser = userContext.getCurrentUser();

        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();

            // Check if user owns the company
            if (company.getUserId() != currentUser.getId()) {
                throw new IllegalStateException("You can only delete your own companies");
            }

            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Map Company entity to DTO
     */
    private CompanyResponseDTO mapToDTO(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getUserId(),
                company.getLatitude(),
                company.getLongitude());
    }
}
