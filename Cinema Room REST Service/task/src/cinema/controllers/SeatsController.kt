package cinema.controllers

import cinema.Seat
import cinema.configuration.CustomException
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

const val TOTAL_ROWS = 9
const val TOTAL_COLUMNS = 9

@RestController
class SeatsController {

    @GetMapping("/seats")
    fun getSeatsInfo(): SeatsInfo {
        return SeatsInfo
    }

    @PostMapping("/purchase")
    fun purchaseOneAvailableSeat(@RequestBody seat: Seat) : Seat {
        seat.price = if (seat.row <= 4) 10 else 8

        if (seat.row > TOTAL_ROWS || seat.column > TOTAL_COLUMNS || seat.row <= 0 || seat.column <= 0) {
            throw CustomException(error = "The number of a row or a column is out of bounds!")
        }

        if (SeatsInfo.availableSeats.contains(seat)) {
            SeatsInfo.availableSeats.remove(seat)
            return seat
        } else {
            throw CustomException(error = "The ticket has been already purchased!")
        }
    }
    
    object SeatsInfo {
        @get:JsonProperty("total_rows")
        val totalRows = TOTAL_ROWS

        @get:JsonProperty("total_columns")
        val totalColumns = TOTAL_COLUMNS

        @get:JsonProperty("available_seats")
        val availableSeats: ArrayList<Seat> = ArrayList<Seat>()

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