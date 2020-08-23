package be.zlz.kara.bin.repositories

import be.zlz.kara.bin.domain.ModuleEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModuleEventRepository: JpaRepository<ModuleEvent, Long>

