package com.isms.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="personnels", catalog="isms_db", schema="", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"personnel_name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personnels.findAll", query = "SELECT s FROM Personnels s"),
    @NamedQuery(name = "Personnels.findById", query = "SELECT s FROM Personnels s WHERE s.id = :id"),
    @NamedQuery(name = "Personnels.findByFirstName", query = "SELECT s FROM Personnels s WHERE s.firstName = :firstName"),
    @NamedQuery(name = "Personnels.findByLastName", query = "SELECT s FROM Personnels s WHERE s.lastName = :lastName"),
    @NamedQuery(name = "Personnels.findByPersonnelName", query = "SELECT s FROM Personnels s WHERE s.personnelName = :personnelName"),
    @NamedQuery(name = "Personnels.findByIdNo", query = "SELECT s FROM Personnels s WHERE s.idNo = :idNo"),
    @NamedQuery(name = "Personnels.findByTelNo", query = "SELECT s FROM Personnels s WHERE s.telNo = :telNo"),
    @NamedQuery(name = "Personnels.findByEmail", query = "SELECT s FROM Personnels s WHERE s.email = :email"),
    @NamedQuery(name = "Personnels.findByGender", query = "SELECT s FROM Personnels s WHERE s.gender = :gender"),
    @NamedQuery(name = "Personnels.findByDetailedResidence", query = "SELECT s FROM Personnels s WHERE s.detailedResidence = :detailedResidence"),
    @NamedQuery(name = "Personnels.findByAchievements", query = "SELECT s FROM Personnels s WHERE s.achievements = :achievements"),
    @NamedQuery(name = "Personnels.findByEmployeeNo", query = "SELECT s FROM Personnels s WHERE s.employeeNo = :employeeNo"),
    @NamedQuery(name = "Personnels.findByNssfNo", query = "SELECT s FROM Personnels s WHERE s.nssfNo = :nssfNo"),
    @NamedQuery(name = "Personnels.findByNhifNo", query = "SELECT s FROM Personnels s WHERE s.nhifNo = :nhifNo"),
    @NamedQuery(name = "Personnels.findByScannedDocument", query = "SELECT s FROM Personnels s WHERE s.scannedDocument = :scannedDocument"),
    @NamedQuery(name = "Personnels.findByPersonnelPicture", query = "SELECT s FROM Personnels s WHERE s.personnelPicture = :personnelPicture"),
    @NamedQuery(name = "Personnels.findByCategories", query = "SELECT s FROM Personnels s WHERE s.categories = :categories"),
    @NamedQuery(name = "Personnels.findByLocations", query = "SELECT s FROM Personnels s WHERE s.locations = :locations")})

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Personnels implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="record_date", nullable=false)
    private Date recordDate;

    @Column(name="first_name", nullable=false)
    private String firstName;
    
    @Column(name="last_name", nullable=false)
    private String lastName;
    
    @Column(name="personnel_name", nullable=false)
    private String personnelName;

    @Column(name="id_no", nullable=false)
    private String idNo;
    
    @Column(name="tel_no", nullable=false)
    private String telNo;

    @Column(name="email", nullable=false)
    private String email;
    
    @Column(name="gender", nullable=false)
    private String gender;

    @Column(name="status", nullable=false)
    private String status;

    @Column(name="detailed_residence", nullable=false, length = 2000)
    private String detailedResidence;
    
    @Column(name="achievements", nullable=false)
    private String achievements;

    @Column(name="salary_amount", nullable=false)
    private Double salaryAmount;

    @Column(name="employee_no", nullable=false)
    private String employeeNo;

    @Column(name="nssf_no", nullable=true)
    private String nssfNo;

    @Column(name="nhif_no", nullable=true)
    private String nhifNo;

    @Column(name="scanned_document", nullable=false, length = 255)
    private String scannedDocument;

    @Column(name = "personnel_picture", nullable = true, length = 255)
    private String personnelPicture;
    
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Categories categories;

    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations locations;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;

    @OneToMany(mappedBy = "personnel")
    @JsonIgnore
    private List<Assignment> installationPersonnels;

    public Personnels() {
    }

    public Personnels(int id, Date recordDate, String firstName, String lastName, String personnelName, String idNo,
            String telNo, String email, String gender, String status, String detailedResidence, String achievements,
            Double salaryAmount, String employeeNo, String nssfNo, String nhifNo, String scannedDocument,
            String personnelPicture, Categories categories, Locations locations) {
        super();
        this.id = id;
        this.recordDate = recordDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personnelName = personnelName;
        this.idNo = idNo;
        this.telNo = telNo;
        this.email = email;
        this.gender = gender;
        this.status = status;
        this.detailedResidence = detailedResidence;
        this.achievements = achievements;
        this.salaryAmount = salaryAmount;
        this.employeeNo = employeeNo;
        this.nssfNo = nssfNo;
        this.nhifNo = nhifNo;
        this.scannedDocument = scannedDocument;
        this.personnelPicture = personnelPicture;
        this.categories = categories;
        this.locations = locations;
    }

    public Personnels(Date recordDate, String firstName, String lastName, String personnelName, String idNo,
            String telNo, String email, String gender, String status, String detailedResidence, String achievements,
            Double salaryAmount, String employeeNo, String nssfNo, String nhifNo, String scannedDocument,
            String personnelPicture, Categories categories, Locations locations) {
        super();
        this.recordDate = recordDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personnelName = personnelName;
        this.idNo = idNo;
        this.telNo = telNo;
        this.email = email;
        this.gender = gender;
        this.status = status;
        this.detailedResidence = detailedResidence;
        this.achievements = achievements;
        this.salaryAmount = salaryAmount;
        this.employeeNo = employeeNo;
        this.nssfNo = nssfNo;
        this.nhifNo = nhifNo;
        this.scannedDocument = scannedDocument;
        this.personnelPicture = personnelPicture;
        this.categories = categories;
        this.locations = locations;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetailedResidence() {
        return detailedResidence;
    }

    public void setDetailedResidence(String detailedResidence) {
        this.detailedResidence = detailedResidence;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(Double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getNssfNo() {
        return nssfNo;
    }

    public void setNssfNo(String nssfNo) {
        this.nssfNo = nssfNo;
    }

    public String getNhifNo() {
        return nhifNo;
    }

    public void setNhifNo(String nhifNo) {
        this.nhifNo = nhifNo;
    }

    public String getScannedDocument() {
        return scannedDocument;
    }

    public void setScannedDocument(String scannedDocument) {
        this.scannedDocument = scannedDocument;
    }

    public String getPersonnelPicture() {
        return personnelPicture;
    }

    public void setPersonnelPicture(String personnelPicture) {
        this.personnelPicture = personnelPicture;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public AuditIdentifier getAuditIdentifierId() {
        return auditIdentifierId;
    }

    public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
        this.auditIdentifierId = auditIdentifierId;
    }
    
    @XmlTransient
    public List<Assignment> getInstallationPersonnels() {
        return installationPersonnels;
    }
    
    public void setInstallationPersonnels(List<Assignment> installationPersonnels) {
        this.installationPersonnels = installationPersonnels;
    }

    @Transient
    public String getPersonnelPicturePath() {
        if(personnelPicture == null || id == 0) return null;
        return "/personnel-pictures/"+id+"/"+personnelPicture;
    }

    @Transient
    public String getScannedDocumentPath() {
        if(scannedDocument == null || id == 0) return null;
        return "/personnel-scanned-documents/"+id+"/"+scannedDocument;
    }

    @Override
    public String toString() {
        return "Personnels [personnelName=" + personnelName + "]";
    }
}