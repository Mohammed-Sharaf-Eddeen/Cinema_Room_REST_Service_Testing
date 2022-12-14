package cinema.controllers

import cinema.Seat
import cinema.configuration.CustomException
import cinema.models.Reservation
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.collections.ArrayList

const val TOTAL_ROWS = 9
const val TOTAL_COLUMNS = 9

@RestController
class SeatsController {

    @GetMapping("/seats")
    fun getSeatsInfo(): SeatsInfo {
        return SeatsInfo
    }

    @PostMapping("/purchase")
    fun purchaseOneAvailableSeat(@RequestBody seat: Seat) : Reservation {
        seat.price = if (seat.row <= 4) 10 else 8

        if (seat.row > TOTAL_ROWS || seat.column > TOTAL_COLUMNS || seat.row <= 0 || seat.column <= 0) {
            throw CustomException("The number of a row or a column is out of bounds!", HttpStatus.BAD_REQUEST)
        }

        if (SeatsInfo.availableSeats.contains(seat)) {
            SeatsInfo.availableSeats.remove(seat)
            val reservation = Reservation(UUID.randomUUID(), seat)
            SeatsInfo.reservations.add(reservation)
            return reservation
        } else {
            throw CustomException("The ticket has been already purchased!", HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/return")
    fun returnOneTicket(@RequestBody request: Map<String, UUID>): Map<String, Seat> {
        val token = request["token"]
        for (reservation in SeatsInfo.reservations) {
            if (reservation.token == token) {
                SeatsInfo.reservations.remove(reservation)
                SeatsInfo.availableSeats.add(reservation.ticket)
                return mapOf<String, Seat>("returned_ticket" to reservation.ticket)
            }
        }
        throw CustomException("Wrong token!", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/stats")
    fun showStats(@RequestParam(defaultValue = "") password: String): Map<String, Int> {
        if (password != "super_secret"){
            throw CustomException("The password is wrong!", HttpStatus.UNAUTHORIZED)
        }

        var currentIncome = 0
        for (reservation in SeatsInfo.reservations) {
            currentIncome += reservation.ticket.price
        }

        return mapOf("current_income" to currentIncome,
            "number_of_available_seats" to SeatsInfo.availableSeats.size,
            "number_of_purchased_tickets" to SeatsInfo.reservations.size
        )
    }
    
    object SeatsInfo {
        @get:JsonProperty("total_rows")
        val totalRows = TOTAL_ROWS

        @get:JsonProperty("total_columns")
        val totalColumns = TOTAL_COLUMNS

        @get:JsonProperty("available_seats")
        val availableSeats: ArrayList<Seat> = ArrayList<Seat>()

        @get:JsonIgnore
        val reservations: ArrayList<Reservation> = ArrayList<Reservation>()

        init {
            var price: Int
            for (i in 1..totalRows) {
                price = if (i <= 4) 10 else 8
                for (j in 1..totalColumns) {
                    availableSeats.add(Seat(i, j, price))
                }
            }
        }
    }
}