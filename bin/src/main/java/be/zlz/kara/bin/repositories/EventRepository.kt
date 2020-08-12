package be.zlz.kara.bin.repositories

import be.zlz.kara.bin.domain.Bin
import be.zlz.kara.bin.domain.Event
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EventRepository: JpaRepository<Event, String> {

    fun getByBinOrderByEventTimeDesc(bin: Bin, pageable: Pageable): Page<Event>

    @Modifying
    @Query(nativeQuery = true, value = "delete from event where bin_id=?1")
    fun deleteAllByBinNative(binId: Long)

}