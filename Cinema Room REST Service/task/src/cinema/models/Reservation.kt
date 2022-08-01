package cinema.models

import cinema.Seat
import java.util.*


class Reservation(val token: UUID, val ticket: Seat) {
}