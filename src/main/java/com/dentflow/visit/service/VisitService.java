package com.dentflow.visit.service;

import com.dentflow.clinic.model.Clinic;
import com.dentflow.clinic.model.ClinicRepository;
import com.dentflow.patient.service.PatientService;
import com.dentflow.user.model.User;
import com.dentflow.user.service.UserService;
import com.dentflow.visit.model.Visit;
import com.dentflow.visit.model.VisitRepository;
import com.dentflow.visit.model.VisitRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private VisitRepository visitRepository;
    private ClinicRepository clinicRepository;
    private UserService userService;
    private PatientService patientService;


    public VisitService(VisitRepository visitRepository,UserService userService,PatientService patientService,ClinicRepository clinicRepository) {
        this.visitRepository = visitRepository;
        this.userService = userService;
        this.patientService = patientService;
        this.clinicRepository = clinicRepository;
    }

    public Set<Visit> getVisitsByClinicId(String email, long clinicId) {
        Clinic clinic = userService.getUser(email).getClinics().stream().filter(c -> c.getId()==clinicId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic not found: "));
        return clinic.getVisits();
    }

    public void addVisitsToClinic(VisitRequest visitRequest, String email) {
        Clinic clinic = userService.getUser(email).getClinics().stream().filter(c -> c.getId()==visitRequest.getClinicId()).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clinic not found"));
        Visit visit = VisitRequest.toEntity(visitRequest);
        visit.setDoctor(userService.getUser(visitRequest.getDoctorEmail()));
        visit.setPatient(patientService.getPatient(visitRequest.getPatientId()));
        visitRepository.save(visit);
        clinic.addVisit(visit);
        clinicRepository.save(clinic);
    }

}