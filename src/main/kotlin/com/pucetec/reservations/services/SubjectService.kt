package com.pucetec.reservations.services

import com.pucetec.reservations.exceptions.ProfessorNotFoundException
import com.pucetec.reservations.exceptions.StudentAlreadyEnrolledException
import com.pucetec.reservations.exceptions.StudentNotFoundException
import com.pucetec.reservations.exceptions.SubjectNotFoundException
import com.pucetec.reservations.mappers.SubjectMapper
import com.pucetec.reservations.models.entities.Subject
import com.pucetec.reservations.models.requests.SubjectRequest
import com.pucetec.reservations.models.responses.SubjectResponse
import com.pucetec.reservations.repositories.ProfessorRepository
import com.pucetec.reservations.repositories.StudentRepository
import com.pucetec.reservations.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository,
    private val studentRepository: StudentRepository,
    private val subjectMapper: SubjectMapper,
) {
    fun createSubject(request: SubjectRequest): SubjectResponse {
        // TODO: Implement the logic to create a subject
        // Step 1: Find the professor by ID
        val professor = professorRepository.findById(request.professorId)
            .orElseThrow{ ProfessorNotFoundException("Professor with ID ${request.professorId} not found.")}
        // Step 2: Create a new Subject entity
        val subject = Subject(
            name = request.name,
            semester = request.semester,
            professor = professor
        )
        // Step 3: Save the subject to the repository
        val savedSubject = subjectRepository.save(subject)
        // Step 4: Return the created subject response
        return subjectMapper.toResponse(savedSubject)
    }



    fun listSubjects(): List<SubjectResponse> =
        subjectMapper.toResponseList(subjectRepository.findAll())
}