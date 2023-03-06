package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.ICustomerSegment;
import com.affordability.customer.segmentation.segments.ICustomerSegmentationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Service
@RequestScope
@Slf4j
@RequiredArgsConstructor
public class CustomerSegmentationAssigner implements ICustomerSegmentationAssigner {

    private final ICustomerSegmentationFactory customerSegmentationFactory;

    @Override
    public ICustomerSegment determineSegment(ICustomerInfo customerInfo) {
        return findFirstAssignableSegmentOrDefault(customerInfo);
    }

    private ICustomerSegment findFirstAssignableSegmentOrDefault(ICustomerInfo customerInfo) {
        return getFirstAssignableSegmentOrNull(customerInfo)
                .orElse(this.customerSegmentationFactory.getNonPrimeSegment());
    }

    private Optional<ICustomerSegment> getFirstAssignableSegmentOrNull(ICustomerInfo customerInfo) {
        var segments = this.customerSegmentationFactory.getAllSegmentsOrderedByPriority();

        return segments.stream()
                .filter(segment -> segment.isCustomerAssignableToSegment(customerInfo))
                .findFirst();
    }
}
