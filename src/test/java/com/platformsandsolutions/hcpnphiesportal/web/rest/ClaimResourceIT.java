package com.platformsandsolutions.hcpnphiesportal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platformsandsolutions.hcpnphiesportal.IntegrationTest;
import com.platformsandsolutions.hcpnphiesportal.domain.CareTeam;
import com.platformsandsolutions.hcpnphiesportal.domain.CareTeamSequence;
import com.platformsandsolutions.hcpnphiesportal.domain.Claim;
import com.platformsandsolutions.hcpnphiesportal.domain.ClaimResponse;
import com.platformsandsolutions.hcpnphiesportal.domain.Coverage;
import com.platformsandsolutions.hcpnphiesportal.domain.Diagnosis;
import com.platformsandsolutions.hcpnphiesportal.domain.DiagnosisSequence;
import com.platformsandsolutions.hcpnphiesportal.domain.Givens;
import com.platformsandsolutions.hcpnphiesportal.domain.HumanName;
import com.platformsandsolutions.hcpnphiesportal.domain.InformationSequence;
import com.platformsandsolutions.hcpnphiesportal.domain.Insurance;
import com.platformsandsolutions.hcpnphiesportal.domain.Item;
import com.platformsandsolutions.hcpnphiesportal.domain.ListRoleCodeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.ListSpecialtyEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.Organization;
import com.platformsandsolutions.hcpnphiesportal.domain.Patient;
import com.platformsandsolutions.hcpnphiesportal.domain.Payee;
import com.platformsandsolutions.hcpnphiesportal.domain.Practitioner;
import com.platformsandsolutions.hcpnphiesportal.domain.PractitionerRole;
import com.platformsandsolutions.hcpnphiesportal.domain.SupportingInfo;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.AdministrativeGenderEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.BodySiteEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CareTeamRoleEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimSubTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ClaimTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.CoverageTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.DiagnosisOnAdmissionEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.DiagnosisTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.FundsReserveEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.MaritalStatusEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.OrganizationTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PayeeTypeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.PriorityEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.RelationShipEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.ReligionEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.RoleCodeEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SpecialtyEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SubSiteEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SupportingInfoCategoryEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.SupportingInfoCodeVisitEnum;
import com.platformsandsolutions.hcpnphiesportal.domain.enumeration.Use;
import com.platformsandsolutions.hcpnphiesportal.repository.ClaimRepository;
import java.io.File;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import platform.fhir_client.models.ClaimModel;
import platform.fhir_client.models.ClaimResponseModel;
import platform.fhir_client.models.CoreResourceModel;
import platform.fhir_client.utils.Constants;
import platform.fhir_client.utils.FHIRHelper;

/**
 * Integration tests for the {@link ClaimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClaimResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_QUEUED = false;
    private static final Boolean UPDATED_IS_QUEUED = true;

    private static final String DEFAULT_PARSED = "AAAAAAAAAA";
    private static final String UPDATED_PARSED = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final Use DEFAULT_USE = Use.Claim;
    private static final Use UPDATED_USE = Use.PreAuthorization;

    private static final ClaimTypeEnum DEFAULT_TYPE = ClaimTypeEnum.Institutional;
    private static final ClaimTypeEnum UPDATED_TYPE = ClaimTypeEnum.Oral;

    private static final ClaimSubTypeEnum DEFAULT_SUB_TYPE = ClaimSubTypeEnum.Ip;
    private static final ClaimSubTypeEnum UPDATED_SUB_TYPE = ClaimSubTypeEnum.Op;

    private static final String DEFAULT_ELIGIBILITY_OFFLINE = "AAAAAAAAAA";
    private static final String UPDATED_ELIGIBILITY_OFFLINE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ELIGIBILITY_OFFLINE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ELIGIBILITY_OFFLINE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_AUTHORIZATION_OFFLINE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AUTHORIZATION_OFFLINE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_BILLABLE_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BILLABLE_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_BILLABLE_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BILLABLE_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PriorityEnum DEFAULT_PRIORITY = PriorityEnum.Stat;
    private static final PriorityEnum UPDATED_PRIORITY = PriorityEnum.Normal;

    private static final FundsReserveEnum DEFAULT_FUNDS_RESERVE = FundsReserveEnum.Patient;
    private static final FundsReserveEnum UPDATED_FUNDS_RESERVE = FundsReserveEnum.Provider;

    private static final String ENTITY_API_URL = "/api/claims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClaimMockMvc;

    private Claim claim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createEntity(EntityManager em) {
        Claim claim = new Claim()
            .guid(DEFAULT_GUID)
            .isQueued(DEFAULT_IS_QUEUED)
            .parsed(DEFAULT_PARSED)
            .identifier(DEFAULT_IDENTIFIER)
            .use(DEFAULT_USE)
            .type(DEFAULT_TYPE)
            .subType(DEFAULT_SUB_TYPE)
            .eligibilityOffline(DEFAULT_ELIGIBILITY_OFFLINE)
            .eligibilityOfflineDate(DEFAULT_ELIGIBILITY_OFFLINE_DATE)
            .authorizationOfflineDate(DEFAULT_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(DEFAULT_BILLABLE_START)
            .billableEnd(DEFAULT_BILLABLE_END)
            .priority(DEFAULT_PRIORITY)
            .fundsReserve(DEFAULT_FUNDS_RESERVE);
        return claim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createUpdatedEntity(EntityManager em) {
        Claim claim = new Claim()
            .guid(UPDATED_GUID)
            .isQueued(UPDATED_IS_QUEUED)
            .parsed(UPDATED_PARSED)
            .identifier(UPDATED_IDENTIFIER)
            .use(UPDATED_USE)
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .eligibilityOffline(UPDATED_ELIGIBILITY_OFFLINE)
            .eligibilityOfflineDate(UPDATED_ELIGIBILITY_OFFLINE_DATE)
            .authorizationOfflineDate(UPDATED_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(UPDATED_BILLABLE_START)
            .billableEnd(UPDATED_BILLABLE_END)
            .priority(UPDATED_PRIORITY)
            .fundsReserve(UPDATED_FUNDS_RESERVE);
        return claim;
    }

    @BeforeEach
    public void initTest() {
        claim = createEntity(em);
    }

    @Test
    @Transactional
    void createClaim() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();
        // Create the Claim
        restClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isCreated());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate + 1);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testClaim.getIsQueued()).isEqualTo(DEFAULT_IS_QUEUED);
        assertThat(testClaim.getParsed()).isEqualTo(DEFAULT_PARSED);
        assertThat(testClaim.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testClaim.getUse()).isEqualTo(DEFAULT_USE);
        assertThat(testClaim.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testClaim.getSubType()).isEqualTo(DEFAULT_SUB_TYPE);
        assertThat(testClaim.getEligibilityOffline()).isEqualTo(DEFAULT_ELIGIBILITY_OFFLINE);
        assertThat(testClaim.getEligibilityOfflineDate()).isEqualTo(DEFAULT_ELIGIBILITY_OFFLINE_DATE);
        assertThat(testClaim.getAuthorizationOfflineDate()).isEqualTo(DEFAULT_AUTHORIZATION_OFFLINE_DATE);
        assertThat(testClaim.getBillableStart()).isEqualTo(DEFAULT_BILLABLE_START);
        assertThat(testClaim.getBillableEnd()).isEqualTo(DEFAULT_BILLABLE_END);
        assertThat(testClaim.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testClaim.getFundsReserve()).isEqualTo(DEFAULT_FUNDS_RESERVE);
    }

    @Test
    @Transactional
    void createClaimWithExistingId() throws Exception {
        // Create the Claim with an existing ID
        claim.setId(1L);

        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUseIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setUse(null);

        // Create the Claim, which fails.

        restClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setType(null);

        // Create the Claim, which fails.

        restClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = claimRepository.findAll().size();
        // set the field null
        claim.setPriority(null);

        // Create the Claim, which fails.

        restClaimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClaims() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get all the claimList
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID)))
            .andExpect(jsonPath("$.[*].isQueued").value(hasItem(DEFAULT_IS_QUEUED.booleanValue())))
            .andExpect(jsonPath("$.[*].parsed").value(hasItem(DEFAULT_PARSED)))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].use").value(hasItem(DEFAULT_USE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subType").value(hasItem(DEFAULT_SUB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].eligibilityOffline").value(hasItem(DEFAULT_ELIGIBILITY_OFFLINE)))
            .andExpect(jsonPath("$.[*].eligibilityOfflineDate").value(hasItem(DEFAULT_ELIGIBILITY_OFFLINE_DATE.toString())))
            .andExpect(jsonPath("$.[*].authorizationOfflineDate").value(hasItem(DEFAULT_AUTHORIZATION_OFFLINE_DATE.toString())))
            .andExpect(jsonPath("$.[*].billableStart").value(hasItem(DEFAULT_BILLABLE_START.toString())))
            .andExpect(jsonPath("$.[*].billableEnd").value(hasItem(DEFAULT_BILLABLE_END.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].fundsReserve").value(hasItem(DEFAULT_FUNDS_RESERVE.toString())));
    }

    @Test
    @Transactional
    void getClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get the claim
        restClaimMockMvc
            .perform(get(ENTITY_API_URL_ID, claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(claim.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID))
            .andExpect(jsonPath("$.isQueued").value(DEFAULT_IS_QUEUED.booleanValue()))
            .andExpect(jsonPath("$.parsed").value(DEFAULT_PARSED))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER))
            .andExpect(jsonPath("$.use").value(DEFAULT_USE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.subType").value(DEFAULT_SUB_TYPE.toString()))
            .andExpect(jsonPath("$.eligibilityOffline").value(DEFAULT_ELIGIBILITY_OFFLINE))
            .andExpect(jsonPath("$.eligibilityOfflineDate").value(DEFAULT_ELIGIBILITY_OFFLINE_DATE.toString()))
            .andExpect(jsonPath("$.authorizationOfflineDate").value(DEFAULT_AUTHORIZATION_OFFLINE_DATE.toString()))
            .andExpect(jsonPath("$.billableStart").value(DEFAULT_BILLABLE_START.toString()))
            .andExpect(jsonPath("$.billableEnd").value(DEFAULT_BILLABLE_END.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.fundsReserve").value(DEFAULT_FUNDS_RESERVE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClaim() throws Exception {
        // Get the claim
        restClaimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    Claim createCompleteClaim() throws Exception {
        Claim claim = new Claim()
            .guid(UUID.randomUUID().toString())
            .isQueued(false)
            .parsed("")
            .identifier("claim-")
            .use(Use.Claim)
            .type(ClaimTypeEnum.Institutional)
            .subType(ClaimSubTypeEnum.Ip)
            .eligibilityOffline("")
            .eligibilityOfflineDate(UPDATED_ELIGIBILITY_OFFLINE_DATE)
            .authorizationOfflineDate(UPDATED_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(UPDATED_BILLABLE_START)
            .billableEnd(UPDATED_BILLABLE_END)
            .priority(PriorityEnum.Normal)
            .fundsReserve(FundsReserveEnum.None);
        Organization provider = new Organization()
            .baseUrl("http://pr-fhir.com.sa")
            .guid(UUID.randomUUID().toString())
            .name("PR-FHIR")
            .organizationLicense("PR-FHIR")
            .organizationType(OrganizationTypeEnum.prov);
        claim.setProvider(provider);
        Organization insurer = new Organization()
            .baseUrl("http://almerys-payer.com.sa")
            .guid(UUID.randomUUID().toString())
            .name("ALMERYS-PAYER")
            .organizationLicense("ALMERYS-PAYER")
            .organizationType(OrganizationTypeEnum.ins);
        claim.setInsurer(insurer);
        HumanName humanName = new HumanName().addGiven(new Givens().given("given").prefix("pat").suffix("ient")).family("family");
        Long id = (long) 1;
        Patient patient = new Patient()
            .id(id)
            .guid(UUID.randomUUID().toString())
            .residentNumber("DEFAULT_RESIDENT_NUMBER")
            // .passportNumber(DEFAULT_PASSPORT_NUMBER)
            // .nationalHealthId(DEFAULT_NATIONAL_HEALTH_ID)
            // .iqama(DEFAULT_IQAMA)
            .religion(ReligionEnum.N1)
            .gender(AdministrativeGenderEnum.male)
            .birthDate(Instant.now().minus(20, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            // .deceasedDate(DEFAULT_DECEASED_DATE)
            .maritalStatus(MaritalStatusEnum.M)
            .addNames(humanName);
        claim.setPatient(patient);
        Set<CareTeam> careTeams = new HashSet<CareTeam>();
        humanName = new HumanName().addGiven(new Givens().given("given").prefix("prac").suffix("titioner")).family("family");
        Practitioner practitioner = new Practitioner()
            .gender(AdministrativeGenderEnum.male)
            .practitionerLicense("practitionerLicense")
            .guid(UUID.randomUUID().toString())
            .addNames(humanName);
        ListRoleCodeEnum code = new ListRoleCodeEnum().r(RoleCodeEnum.Doctor);
        ListSpecialtyEnum listSpecialtyEnum = new ListSpecialtyEnum().s(SpecialtyEnum.DOT01_00);
        PractitionerRole practitionerRole = new PractitionerRole()
            .practitioner(practitioner)
            .guid(UUID.randomUUID().toString())
            .addCodes(code)
            .addSpecialties(listSpecialtyEnum)
            .organization(provider);
        CareTeam c = new CareTeam()
            .providerRole(practitionerRole)
            .role(CareTeamRoleEnum.Primary)
            .sequence(1)
            .qualification(SpecialtyEnum.DOT01_00);
        careTeams.add(c);
        claim.setCareTeams(careTeams);
        Set<Diagnosis> diagnoses = new HashSet<Diagnosis>();
        Diagnosis diag = new Diagnosis()
            .diagnosis("A00.0")
            .onAdmission(DiagnosisOnAdmissionEnum.N)
            .sequence(1)
            .type(DiagnosisTypeEnum.Principal);
        diagnoses.add(diag);
        claim.setDiagnoses(diagnoses);
        Set<Insurance> insurances = new HashSet<Insurance>();
        Coverage coverage = new Coverage()
            .id(id)
            .beneficiary(patient)
            .coverageType(CoverageTypeEnum.EHCPOL)
            .dependent("dependent")
            .guid(UUID.randomUUID().toString())
            .network("network")
            .payor(insurer)
            .relationShip(RelationShipEnum.self)
            .subrogation(false)
            .subscriberId("subscriberId")
            .subscriberPatient(patient);
        Insurance insurance = new Insurance().coverage(coverage).focal(true).preAuthRef("preAuthRef").sequence(1);
        insurances.add(insurance);
        claim.setInsurances(insurances);
        SupportingInfo sup = new SupportingInfo()
            .sequence(1)
            .category(SupportingInfoCategoryEnum.Attachment)
            .codeVisit(SupportingInfoCodeVisitEnum.Referral)
            .timing(Instant.now().minus(20, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS));
        claim.addSupportingInfos(sup);
        Payee payee = new Payee().type(PayeeTypeEnum.Provider);
        claim.setPayee(payee);
        Item item = new Item()
            .sequence(1)
            .isPackage(false)
            .tax(new BigDecimal(10))
            // .payerShare(new BigDecimal(10))
            .patientShare(new BigDecimal(10))
            .transportationSRCA("83500-00-00")
            // .imaging(UPDATED_IMAGING)
            // .laboratory(UPDATED_LABORATORY)
            // .medicalDevice(UPDATED_MEDICAL_DEVICE)
            // .oralHealthIP(UPDATED_ORAL_HEALTH_IP)
            // .oralHealthOP(UPDATED_ORAL_HEALTH_OP)
            // .procedure(UPDATED_PROCEDURE)
            // .services(UPDATED_SERVICES)
            // .medicationCode(UPDATED_MEDICATION_CODE)
            // .servicedDate(Instant.now().minus(20,
            // ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateStart(Instant.now().minus(20, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateEnd(Instant.now().minus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .quantity(2)
            .unitPrice(25)
            .factor(new BigDecimal(0.95))
            .bodySite(BodySiteEnum.E1)
            .subSite(SubSiteEnum.A);
        Set<CareTeamSequence> careTeamSequences = new HashSet<CareTeamSequence>();
        CareTeamSequence cts = new CareTeamSequence().careSeq(1);
        careTeamSequences.add(cts);
        item.setCareTeamSequences(careTeamSequences);
        Set<DiagnosisSequence> diagnosisSequences = new HashSet<DiagnosisSequence>();
        DiagnosisSequence d = new DiagnosisSequence().diagSeq(1);
        diagnosisSequences.add(d);
        item.setDiagnosisSequences(diagnosisSequences);
        Set<InformationSequence> informationSequences = new HashSet<InformationSequence>();
        InformationSequence informationSequence = new InformationSequence().infSeq(1);
        informationSequences.add(informationSequence);
        item.setInformationSequences(informationSequences);
        Item item2 = new Item()
            .sequence(1)
            .isPackage(false)
            .tax(new BigDecimal(10))
            // .payerShare(new BigDecimal(10))
            .patientShare(new BigDecimal(10))
            // .transportationSRCA("83500-00-00")
            .imaging("58900-00-90")
            // .laboratory(UPDATED_LABORATORY)
            // .medicalDevice(UPDATED_MEDICAL_DEVICE)
            // .oralHealthIP(UPDATED_ORAL_HEALTH_IP)
            // .oralHealthOP(UPDATED_ORAL_HEALTH_OP)
            // .procedure(UPDATED_PROCEDURE)
            // .services(UPDATED_SERVICES)
            // .medicationCode(UPDATED_MEDICATION_CODE)
            // .servicedDate(Instant.now().minus(20,
            // ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateStart(Instant.now().minus(20, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateEnd(Instant.now().minus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .quantity(2)
            .unitPrice(25)
            .factor(new BigDecimal(0.95))
            .bodySite(BodySiteEnum.E1)
            .subSite(SubSiteEnum.A);
        item2.setCareTeamSequences(careTeamSequences);
        item2.setDiagnosisSequences(diagnosisSequences);
        item2.setInformationSequences(informationSequences);
        Item item3 = new Item()
            .sequence(1)
            .isPackage(false)
            .tax(new BigDecimal(10))
            // .payerShare(new BigDecimal(10))
            .patientShare(new BigDecimal(10))
            // .transportationSRCA("83500-00-00")
            // .imaging("58900-00-90")
            .laboratory("73250-00-80")
            // .medicalDevice(UPDATED_MEDICAL_DEVICE)
            // .oralHealthIP(UPDATED_ORAL_HEALTH_IP)
            // .oralHealthOP(UPDATED_ORAL_HEALTH_OP)
            // .procedure(UPDATED_PROCEDURE)
            // .services(UPDATED_SERVICES)
            // .medicationCode(UPDATED_MEDICATION_CODE)
            // .servicedDate(Instant.now().minus(20,
            // ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateStart(Instant.now().minus(20, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .servicedDateEnd(Instant.now().minus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MILLIS))
            .quantity(2)
            .unitPrice(25)
            .factor(new BigDecimal(0.95))
            .bodySite(BodySiteEnum.E1)
            .subSite(SubSiteEnum.A);
        item3.setCareTeamSequences(careTeamSequences);
        item3.setDiagnosisSequences(diagnosisSequences);
        item3.setInformationSequences(informationSequences);
        Set<Item> items = new HashSet<Item>();
        items.add(item);
        items.add(item2);
        items.add(item3);
        claim.setItems(items);
        return claim;
    }

    @Test
    void sendClaim() throws Exception {
        Claim claim = createCompleteClaim();
        FHIRHelper fhirHelper = new FHIRHelper();

        String path = System.getProperty("user.dir");
        String fileName = (new File(path, "PR-FHIR.p12")).getPath();
        try {
            fhirHelper.useServerWithProviderCertificate(
                Constants.SERVER_URL,
                Constants.SERVER_VERSION,
                fileName,
                "puyjdDe4A2dh",
                "http://www.pr-fhir.sa"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CoreResourceModel> coreResources1 = new ArrayList<CoreResourceModel>();
        coreResources1.add(fhirHelper.getSender());
        ClaimModel cl = claim.convert(coreResources1);
        cl.setProvider(fhirHelper.getSender());

        fhirHelper.initCoreResources(coreResources1);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        String json2 = "";

        fhirHelper.sendMessage(cl, false);
        try {
            json = mapper.writeValueAsString(cl);
            json2 = mapper.writeValueAsString(fhirHelper.getInputResources().entrySet().iterator().next().getValue());
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        if (fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest() != null) {
            claim.setParsed(fhirHelper.getInputResources().entrySet().iterator().next().getValue().getParsedRequest());
            // claimRepository.save(claim);
        }

        if (fhirHelper.getOutputResources() != null && fhirHelper.getOutputResources().size() > 0) {
            ClaimResponseModel model = fhirHelper.getClaimResponses(true).get(0);
            ClaimResponse claimResponse = ClaimResponse.convertFrom(model);
        }
    }

    @Test
    @Transactional
    void putNewClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim
        Claim updatedClaim = claimRepository.findById(claim.getId()).get();
        // Disconnect from session so that the updates on updatedClaim are not directly
        // saved in db
        em.detach(updatedClaim);
        updatedClaim
            .guid(UPDATED_GUID)
            .isQueued(UPDATED_IS_QUEUED)
            .parsed(UPDATED_PARSED)
            .identifier(UPDATED_IDENTIFIER)
            .use(UPDATED_USE)
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .eligibilityOffline(UPDATED_ELIGIBILITY_OFFLINE)
            .eligibilityOfflineDate(UPDATED_ELIGIBILITY_OFFLINE_DATE)
            .authorizationOfflineDate(UPDATED_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(UPDATED_BILLABLE_START)
            .billableEnd(UPDATED_BILLABLE_END)
            .priority(UPDATED_PRIORITY)
            .fundsReserve(UPDATED_FUNDS_RESERVE);

        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClaim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClaim))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testClaim.getIsQueued()).isEqualTo(UPDATED_IS_QUEUED);
        assertThat(testClaim.getParsed()).isEqualTo(UPDATED_PARSED);
        assertThat(testClaim.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testClaim.getUse()).isEqualTo(UPDATED_USE);
        assertThat(testClaim.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClaim.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testClaim.getEligibilityOffline()).isEqualTo(UPDATED_ELIGIBILITY_OFFLINE);
        assertThat(testClaim.getEligibilityOfflineDate()).isEqualTo(UPDATED_ELIGIBILITY_OFFLINE_DATE);
        assertThat(testClaim.getAuthorizationOfflineDate()).isEqualTo(UPDATED_AUTHORIZATION_OFFLINE_DATE);
        assertThat(testClaim.getBillableStart()).isEqualTo(UPDATED_BILLABLE_START);
        assertThat(testClaim.getBillableEnd()).isEqualTo(UPDATED_BILLABLE_END);
        assertThat(testClaim.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testClaim.getFundsReserve()).isEqualTo(UPDATED_FUNDS_RESERVE);
    }

    @Test
    @Transactional
    void putNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, claim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(claim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(claim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClaimWithPatch() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim using partial update
        Claim partialUpdatedClaim = new Claim();
        partialUpdatedClaim.setId(claim.getId());

        partialUpdatedClaim
            .isQueued(UPDATED_IS_QUEUED)
            .parsed(UPDATED_PARSED)
            .subType(UPDATED_SUB_TYPE)
            .authorizationOfflineDate(UPDATED_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(UPDATED_BILLABLE_START)
            .billableEnd(UPDATED_BILLABLE_END)
            .fundsReserve(UPDATED_FUNDS_RESERVE);

        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClaim))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testClaim.getIsQueued()).isEqualTo(UPDATED_IS_QUEUED);
        assertThat(testClaim.getParsed()).isEqualTo(UPDATED_PARSED);
        assertThat(testClaim.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testClaim.getUse()).isEqualTo(DEFAULT_USE);
        assertThat(testClaim.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testClaim.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testClaim.getEligibilityOffline()).isEqualTo(DEFAULT_ELIGIBILITY_OFFLINE);
        assertThat(testClaim.getEligibilityOfflineDate()).isEqualTo(DEFAULT_ELIGIBILITY_OFFLINE_DATE);
        assertThat(testClaim.getAuthorizationOfflineDate()).isEqualTo(UPDATED_AUTHORIZATION_OFFLINE_DATE);
        assertThat(testClaim.getBillableStart()).isEqualTo(UPDATED_BILLABLE_START);
        assertThat(testClaim.getBillableEnd()).isEqualTo(UPDATED_BILLABLE_END);
        assertThat(testClaim.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testClaim.getFundsReserve()).isEqualTo(UPDATED_FUNDS_RESERVE);
    }

    @Test
    @Transactional
    void fullUpdateClaimWithPatch() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim using partial update
        Claim partialUpdatedClaim = new Claim();
        partialUpdatedClaim.setId(claim.getId());

        partialUpdatedClaim
            .guid(UPDATED_GUID)
            .isQueued(UPDATED_IS_QUEUED)
            .parsed(UPDATED_PARSED)
            .identifier(UPDATED_IDENTIFIER)
            .use(UPDATED_USE)
            .type(UPDATED_TYPE)
            .subType(UPDATED_SUB_TYPE)
            .eligibilityOffline(UPDATED_ELIGIBILITY_OFFLINE)
            .eligibilityOfflineDate(UPDATED_ELIGIBILITY_OFFLINE_DATE)
            .authorizationOfflineDate(UPDATED_AUTHORIZATION_OFFLINE_DATE)
            .billableStart(UPDATED_BILLABLE_START)
            .billableEnd(UPDATED_BILLABLE_END)
            .priority(UPDATED_PRIORITY)
            .fundsReserve(UPDATED_FUNDS_RESERVE);

        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClaim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClaim))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testClaim.getIsQueued()).isEqualTo(UPDATED_IS_QUEUED);
        assertThat(testClaim.getParsed()).isEqualTo(UPDATED_PARSED);
        assertThat(testClaim.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testClaim.getUse()).isEqualTo(UPDATED_USE);
        assertThat(testClaim.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testClaim.getSubType()).isEqualTo(UPDATED_SUB_TYPE);
        assertThat(testClaim.getEligibilityOffline()).isEqualTo(UPDATED_ELIGIBILITY_OFFLINE);
        assertThat(testClaim.getEligibilityOfflineDate()).isEqualTo(UPDATED_ELIGIBILITY_OFFLINE_DATE);
        assertThat(testClaim.getAuthorizationOfflineDate()).isEqualTo(UPDATED_AUTHORIZATION_OFFLINE_DATE);
        assertThat(testClaim.getBillableStart()).isEqualTo(UPDATED_BILLABLE_START);
        assertThat(testClaim.getBillableEnd()).isEqualTo(UPDATED_BILLABLE_END);
        assertThat(testClaim.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testClaim.getFundsReserve()).isEqualTo(UPDATED_FUNDS_RESERVE);
    }

    @Test
    @Transactional
    void patchNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, claim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(claim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(claim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();
        claim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeDelete = claimRepository.findAll().size();

        // Delete the claim
        restClaimMockMvc
            .perform(delete(ENTITY_API_URL_ID, claim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
