package be.zlz.kara.bin.dto.converter

interface Converter<T, U> {

    fun toDto(t: T): U

    fun toDomain(u: U): T
}