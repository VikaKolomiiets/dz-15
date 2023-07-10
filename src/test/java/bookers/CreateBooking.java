package bookers;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBooking {
    private Number bookingid;
    private Booking booking;
}
