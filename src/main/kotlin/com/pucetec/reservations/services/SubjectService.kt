package com.pucetec.reservations.services

import com.pucetec.reservations.exceptions.ProfessorNotFoundException
import com.pucetec.reservations.exceptions.StudentAlreadyEnrolledException
import com.pucetec.reservations.exceptions.StudentNotFoundException
import com.pucetec.reservations.exceptions.SubjectNotFoundException
import com.pucetec.reservations.exceptions.SubjectNotFoundExceptionDelete
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

    fun enrollStudent(subjectId: Long, studentId: Long): SubjectResponse {
        // TODO: Implement the logic to enroll a student in a subject
        // Step 1: Find the subject by ID
        val subject = subjectRepository.findById(subjectId)
            .orElseThrow { SubjectNotFoundException("Subject with ID $subjectId not found.") }
        // Step 2: Find the student by ID
        val student = studentRepository.findById(studentId)
            .orElseThrow { StudentNotFoundException("Student with ID $studentId not found.") }

        // Step 3: Check if the student is already enrolled in the subject
        if (subject.students.contains(student)){
            throw StudentAlreadyEnrolledException("Student with already enrolled in subject with ID $subjectId.}")
        }
        // Step 4: If not, enroll the student in the subject
        subject.students.add(student)
        // Step 5: Return the updated subject response
        val updatedSubject = subjectRepository.save(subject)
        return subjectMapper.toResponse(updatedSubject)
    }

    fun deleteSubject(subjectId: Long) {
        val subject = subjectRepository.findById(subjectId)
            .orElseThrow { SubjectNotFoundExceptionDelete("Subject with ID $subjectId not found.") }
        subjectRepository.delete(subject)
    }


    fun listSubjects(): List<SubjectResponse> =
        subjectMapper.toResponseList(subjectRepository.findAll())
}