package com.sliit.weddingplanner.service.impl;

import com.sliit.weddingplanner.dto.PackageDTO;
import com.sliit.weddingplanner.dto.VendorDTO;
import com.sliit.weddingplanner.repository.PackageRepository;
import com.sliit.weddingplanner.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PackageServiceImplTest {

    private final PackageRepository packageRepository = mock(PackageRepository.class);
    private final VendorRepository vendorRepository = mock(VendorRepository.class);
    private final PackageServiceImpl packageService = new PackageServiceImpl(packageRepository, vendorRepository);

    @Test
    void assignsPackageToAuthenticatedVendorInsteadOfSubmittedOwner() {
        VendorDTO vendor = new VendorDTO();
        vendor.setStatus("APPROVED");
        vendor.setAvailability("AVAILABLE");
        when(vendorRepository.findById(7)).thenReturn(Optional.of(vendor));
        when(packageRepository.save(any(PackageDTO.class))).thenAnswer(invocation -> invocation.getArgument(0));
        PackageDTO submitted = new PackageDTO();
        submitted.setVendorId(99);

        PackageDTO created = packageService.createForVendor(submitted, 7);

        assertEquals(7, created.getVendorId());
        verify(packageRepository).save(submitted);
    }

    @Test
    void preventsVendorFromUpdatingAnotherVendorsPackage() {
        VendorDTO vendor = new VendorDTO();
        vendor.setStatus("APPROVED");
        vendor.setAvailability("AVAILABLE");
        PackageDTO packageDTO = new PackageDTO();
        packageDTO.setPackageId(3);
        packageDTO.setVendorId(99);
        when(vendorRepository.findById(7)).thenReturn(Optional.of(vendor));
        when(packageRepository.findById(3)).thenReturn(Optional.of(packageDTO));

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> packageService.updateForVendor(3, new PackageDTO(), 7));

        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        verify(packageRepository, never()).update(any());
    }
}
