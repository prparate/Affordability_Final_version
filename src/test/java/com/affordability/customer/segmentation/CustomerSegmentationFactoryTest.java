package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.CustomerSegmentationFactory;
import com.affordability.customer.segmentation.segments.ICustomerSegment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerSegmentationFactoryTest {

    @Test
    @DisplayName("Should return a list of Segments in a specific order")
    void shouldReturnAListOfSegmentsInASpecificOrder(){

        var expectedOrderedSegmentIdentifiers = new ECustomerSegment[] {
                ECustomerSegment.NOT_CLASSIFIED,
                ECustomerSegment.PRIME,
                ECustomerSegment.NON_PRIME};

        var factory = new CustomerSegmentationFactory();
        var segments = factory.getAllSegmentsOrderedByPriority();

        assertListOfSegments(expectedOrderedSegmentIdentifiers, segments);
    }

    private void assertListOfSegments(ECustomerSegment[] expectedOrderedSegmentIdentifiers, List<ICustomerSegment> segments) {
        var totalExpectedSegments = expectedOrderedSegmentIdentifiers.length;
        var totalSegments = segments.size();

        assertTotalExpectedSegments(totalExpectedSegments, totalSegments);
        assertListOfSegmentsInOrder(expectedOrderedSegmentIdentifiers, segments);
    }

    private void assertListOfSegmentsInOrder(ECustomerSegment[] expectedOrderedSegmentIdentifiers, List<ICustomerSegment> segments) {
        var i = 0;

        for (ICustomerSegment segment : segments) {
            var identifier = segment.getIdentifier();
            var expectedIdentifier = expectedOrderedSegmentIdentifiers[i];

            var expectedSegmentOrderMessage = "The segment with identifier '%s' was expected in the position/index '%d', but '%s' was found instead of."
                                                    .formatted(expectedIdentifier, i, identifier);
            assertEquals(expectedIdentifier, identifier, expectedSegmentOrderMessage);

            i++;
        }
    }

    private void assertTotalExpectedSegments(long totalExpectedSegments, long totalSegments) {
        var differentTotalSegmentsMessage = "Total number of expected segments '%d' , but '%d' found."
                                                    .formatted(totalExpectedSegments, totalSegments);

        assertEquals(totalExpectedSegments, totalSegments, differentTotalSegmentsMessage);
    }
}
