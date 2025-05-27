package com.pucetec.reservations.services

import com.pucetec.reservations.exceptions.ProfessorAlreadyExistsException
import com.pucetec.reservations.mappers.ProfessorMapper
import com.pucetec.reservations.models.entities.Professor
import com.pucetec.reservations.models.responses.ProfessorResponse
import com.pucetec.reservations.repositories.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository,
    private val professorMapper: ProfessorMapper
) {
    fun createProfessor(professor: Professor): ProfessorResponse {
        val existingProfessor: Professor? = professorRepository.findByName(professor.name)
        if (existingProfessor == null) {
            val savedProfessor = professorRepository.save(professor)
            return professorMapper.toResponse(savedProfessor)
        }
        throw ProfessorAlreadyExistsException("Professor with name ${professor.name} already exists")
    }

    fun listProfessors(): List<ProfessorResponse> {
        val professors = professorRepository.findAll()
        return professorMapper.toResponseList(professors)
    }
}