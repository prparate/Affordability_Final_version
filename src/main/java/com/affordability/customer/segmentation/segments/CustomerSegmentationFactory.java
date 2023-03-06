package com.affordability.customer.segmentation.segments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerSegmentationFactory implements ICustomerSegmentationFactory {

    private List<ICustomerSegment> segmentsOrderedByPriority = null;

    @Override
    public List<ICustomerSegment> getAllSegmentsOrderedByPriority() {

        if (this.segmentsOrderedByPriority == null) {
            this.initializeSegmentsInOrder();
        }

        return this.segmentsOrderedByPriority;
    }

    @Override
    public ICustomerSegment getNonPrimeSegment() {
        return new NonPrimeSegment();
    }

    private void initializeSegmentsInOrder() {
        this.segmentsOrderedByPriority = new ArrayList<>();
        this.addNotClassifiedSegment();
        this.addPrimeCustomerSegment();
        this.addNonPrimeSegment();
    }

    private void addNotClassifiedSegment() {
        ICustomerSegment segment = new NotClassifiedSegment();
        this.segmentsOrderedByPriority.add(segment);
    }

    private void addNonPrimeSegment() {
        ICustomerSegment segment = new NonPrimeSegment();
        this.segmentsOrderedByPriority.add(segment);
    }
    private void addPrimeCustomerSegment() {
        ICustomerSegment segment = new PrimeSegment();
        this.segmentsOrderedByPriority.add(segment);
    }
}
