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
@Table(name="equipments", catalog="isms_db", schema="", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"equipment_name"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipments.findAll", query = "SELECT s FROM Equipments s"),
    @NamedQuery(name = "Equipments.findById", query = "SELECT s FROM Equipments s WHERE s.id = :id"),
    @NamedQuery(name = "Equipments.findByEquipmentName", query = "SELECT s FROM Equipments s WHERE s.equipmentName = :equipmentName"),
    @NamedQuery(name = "Equipments.findBySerialNumber", query = "SELECT s FROM Equipments s WHERE s.serialNumber = :serialNumber"),
    @NamedQuery(name = "Equipments.findByStatus", query = "SELECT s FROM Equipments s WHERE s.status = :status"),
    @NamedQuery(name = "Equipments.findByEquipmentPicture", query = "SELECT s FROM Equipments s WHERE s.equipmentPicture = :equipmentPicture"),
    @NamedQuery(name = "Equipments.findByCategories", query = "SELECT s FROM Equipments s WHERE s.categories = :categories")})

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Equipments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name="record_date", nullable=false)
    private Date recordDate;

    @Column(name="equipment_name", nullable=false)
    private String equipmentName;

    @Column(name="serial-number", nullable=false)
    private String serialNumber;
    
    @Column(name="status", nullable=false)
    private String status;
    
    @Column(name="equipment_value", nullable=false)
    private Double equipmentValue;

    @Column(name = "equipment_picture", nullable = true, length = 255)
    private String equipmentPicture;
    
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Categories categories;
    
    @JoinColumn(name = "audit_identifier_id", referencedColumnName = "identifier_id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private AuditIdentifier auditIdentifierId;

    @OneToMany(mappedBy = "equipment")
    @JsonIgnore
    private List<Assignment> installationEquipments;

    public Equipments() {
    }


    public Equipments(int id, Date recordDate, String equipmentName, String serialNumber, String status,
			Double equipmentValue, String equipmentPicture, Categories categories,
			List<Assignment> installationEquipments) {
		super();
		this.id = id;
		this.recordDate = recordDate;
		this.equipmentName = equipmentName;
		this.serialNumber = serialNumber;
		this.status = status;
		this.equipmentValue = equipmentValue;
		this.equipmentPicture = equipmentPicture;
		this.categories = categories;
		this.installationEquipments = installationEquipments;
	}


	public Equipments(Date recordDate, String equipmentName, String serialNumber, String status, Double equipmentValue,
			String equipmentPicture, Categories categories, List<Assignment> installationEquipments) {
		super();
		this.recordDate = recordDate;
		this.equipmentName = equipmentName;
		this.serialNumber = serialNumber;
		this.status = status;
		this.equipmentValue = equipmentValue;
		this.equipmentPicture = equipmentPicture;
		this.categories = categories;
		this.installationEquipments = installationEquipments;
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


	public String getEquipmentName() {
		return equipmentName;
	}


	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}


	public String getSerialNumber() {
		return serialNumber;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Double getEquipmentValue() {
		return equipmentValue;
	}


	public void setEquipmentValue(Double equipmentValue) {
		this.equipmentValue = equipmentValue;
	}


	public String getEquipmentPicture() {
		return equipmentPicture;
	}


	public void setEquipmentPicture(String equipmentPicture) {
		this.equipmentPicture = equipmentPicture;
	}


	public Categories getCategories() {
		return categories;
	}


	public void setCategories(Categories categories) {
		this.categories = categories;
	}


	public AuditIdentifier getAuditIdentifierId() {
		return auditIdentifierId;
	}


	public void setAuditIdentifierId(AuditIdentifier auditIdentifierId) {
		this.auditIdentifierId = auditIdentifierId;
	}


	public List<Assignment> getInstallationEquipments() {
		return installationEquipments;
	}


	public void setInstallationEquipments(List<Assignment> installationEquipments) {
		this.installationEquipments = installationEquipments;
	}


	@Transient
    public String getEquipmentPicturePath() {
        if(equipmentPicture == null || id == 0) return null;
        return "/equipment-pictures/"+id+"/"+equipmentPicture;
    }


    @Override
    public String toString() {
        return "Equipments [equipmentName=" + equipmentName + "]";
    }
}