package cinema.controllers

import cinema.Seat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

const val TOTAL_ROWS = 9
const val TOTAL_COLUMNS = 9

@RestController
class SeatsController {

    @GetMapping("/seats")
    fun getSeatsInfo(): SeatsInfo {
        return SeatsInfo
    }
    
    object SeatsInfo {
        @get:JsonProperty("total_rows")
        val totalRows = TOTAL_ROWS

        @get:JsonProperty("total_columns")
        val totalColumns = TOTAL_COLUMNS

        @get:JsonProperty("available_seats")
        val availableSeats: ArrayList<Seat> = ArrayList<Seat>()

        init {
            for (i in 1..totalRows) {
                for (j in 1..totalColumns) {
                    availableSeats.add(Seat(i, j))
                }
            }
        }
    }
}