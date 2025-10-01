package com.isms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.isms.dto.BillingsDTO;
import com.isms.filter.ClientsReportDataFilter;
import com.isms.model.CarwashStatement_Report;
import com.isms.model.ClientList_Report;
import com.isms.model.Commissions_Report;
import com.isms.model.PaymentJobItem;
import com.isms.model.PaymentReceipt_Report;

public interface PaymentJobItemRepository
		extends JpaRepository<PaymentJobItem, Integer>, JpaSpecificationExecutor<PaymentJobItem> {

	@Modifying
	@Query(value = "truncate table payment_job_items", nativeQuery = true)
	void truncatePaymentJobItem();

	@Modifying
	@Query(value = "INSERT INTO \r\n" + "payment_job_items  \r\n"
			+ "(record_date, client_id, personnel_id, payment_id, job_item_id, cash_in_id, cash_out_id, amount) \r\n"
			+ "SELECT payment_date, client_id, '0', id, '0', '0', '0', amount_paid\r\n" 
			+ "FROM payments ;\r\n"
			+ " ", nativeQuery = true)
	void fillPaymentJobItem_Payments();

	@Modifying
	@Query(value = "INSERT INTO \r\n" + "payment_job_items  \r\n"
			+ "(record_date, client_id, personnel_id, payment_id, job_item_id, cash_in_id, cash_out_id, amount) \r\n"
			+ "SELECT created_at, client_id, '0', '0', id, '0', '0', -total_cost \r\n" 
			+ "FROM job_items ;\r\n"
			+ " ", nativeQuery = true)
	void fillPaymentJobItem_JobItem();
	
	@Modifying
	@Query(value = "INSERT INTO \r\n" + "payment_job_items  \r\n"
			+ "(record_date, client_id, personnel_id, payment_id, job_item_id, cash_in_id, cash_out_id, amount) \r\n"
			+ "SELECT record_date, COALESCE(client_id,0), COALESCE(personnel_id,0), '0', '0', id, '0', amount \r\n" 
			+ "FROM cash_in ;\r\n"
			+ " ", nativeQuery = true)
	void fillPaymentJobItem_CashIn();
	
	@Modifying
	@Query(value = "INSERT INTO \r\n" + "payment_job_items  \r\n"
			+ "(record_date, client_id, personnel_id, payment_id, job_item_id, cash_in_id, cash_out_id, amount) \r\n"
			+ "SELECT record_date, COALESCE(client_id,0), COALESCE(personnel_id,0), '0', '0', '0', id, -amount \r\n" 
			+ "FROM cash_out ;\r\n"
			+ " ", nativeQuery = true)
	void fillPaymentJobItem_CashOut();

	@Query("SELECT new com.isms.dto.BillingsDTO(" +
		       "c.id, c.clientName, COALESCE(SUM(pj.amount),0)) " +
		       "FROM PaymentJobItem pj " +
		       "LEFT JOIN Clients c ON pj.clientId = c.id " +
		       "WHERE pj.clientId = :id " +
		       "GROUP BY c.id")
	public BillingsDTO findPaymentJobItem(@Param("id") int id);



	@Query("SELECT new com.isms.model.PaymentJobItem(pj.id, date(pj.recordDate), c.id, s.id, pj.paymentId, pj.jobItemId, pj.cashInId, pj.cashOutId, pj.amount) \r\n"
			+ "FROM PaymentJobItem pj \r\n" 
			+ "LEFT JOIN Payments p ON pj.paymentId = p.id \r\n"
			+ "LEFT JOIN JobItem j ON pj.jobItemId = j.id \r\n" 
			+ "LEFT JOIN CashIn ci ON pj.cashInId = ci.id \r\n" 
			+ "LEFT JOIN CashOut co ON pj.cashOutId = co.id \r\n"
			+ "LEFT JOIN Clients c ON pj.clientId = c.id \r\n"
			+ "LEFT JOIN Personnels s ON pj.personnelId = s.id \r\n"
			+ "ORDER BY pj.recordDate DESC ")
	public List<PaymentJobItem> getAllPaymentJobItem_Response();

//	@Query("SELECT new com.cradletechnologies.cradlecarwash.mis.entities.PaymentJobItem(pj.id, pj.recordDate, c.clientName, pj.paymentId, pj.jobItemId, pj.amount) \r\n"			
//			+"FROM PaymentJobItem pj \r\n"
//			+"LEFT JOIN Payments p ON pj.paymentId = p.id \r\n"
//			+"LEFT JOIN JobItems j ON pj.jobItemId = j.id \r\n"
//			+"LEFT JOIN Clients h ON pj.clientId = h.id \r\n"
//			+"ORDER BY pj.recordDate DESC ")
//	public Page<PaymentJobItem> getAllPaymentJobItem_Response(PaymentJobItemDataFilter paymentJobItemDataFilter,
//			Pageable pageable);

	@Query("SELECT new com.isms.model.ClientList_Report(c.id, COALESCE(MAX(c.clientName), MAX(s.personnelName)), MAX(c.telNo), ( COALESCE(SUM(pj.amount),0.0)) )\r\n"
			+ "		FROM  PaymentJobItem pj\r\n"
			+ "		LEFT OUTER JOIN Clients c ON pj.clientId = c.id\r\n" 
			+ "		LEFT OUTER JOIN Personnels s ON pj.personnelId = s.id \r\n"
			+ "		GROUP BY c.id\r\n"
			+ "		ORDER BY c.id ASC")
	public List<ClientList_Report> getClientListReport();

	
//	@Query("SELECT DISTINCT new com.isms.model.CarwashStatement_Report(date(pj.recordDate), COALESCE(-SUM(j.totalAmount),0), COALESCE(SUM(p.amountPaid),0), COALESCE(-SUM(j.totalAmount),0) + COALESCE(SUM(p.amountPaid),0))\r\n"
//			+ "			FROM PaymentJobItem pj \r\n"
//			+ "			LEFT JOIN JobItem j ON pj.jobItemId = j.id \r\n"
//			+ "			LEFT JOIN Payments p ON pj.paymentId = p.id \r\n"
//			+ "			GROUP BY date(pj.recordDate) \r\n"
//			+ "			ORDER BY date(pj.recordDate) ASC")
//	public List<CarwashStatement_Report> getCarwashStatementReport();
	
		
//	@Query("SELECT DISTINCT new com.isms.model.Commissions_Report(\r\n"
//			+ "			    date(pj.recordDate),\r\n"
//			+ "			    COALESCE(-SUM(j.totalAmount) * 1.0, 0.0),\r\n"
//			+ "			    COALESCE( SUM(p.amountPaid) * 1.0, 0.0),\r\n"
//			+ "			    COALESCE( SUM(p.amountPaid)    * 0.6, 0.0),\r\n"
//			+ "			    COALESCE(-SUM(co.amount)    * 1.0, 0.0),\r\n"
//			+ "			    COALESCE(-SUM(j.totalAmount) * 0.4, 0.0),\r\n"
//			+ "			    COALESCE( SUM(p.amountPaid)  * 0.4, 0.0),\r\n"
//			+ "			    COALESCE( SUM(co.amount)     * 1.0, 0.0)\r\n"
//			+ "			)\r\n"
//			+ "			FROM PaymentJobItem pj\r\n"
//			+ "			LEFT JOIN JobItem j ON pj.jobItemId = j.id\r\n"
//			+ "			LEFT JOIN Payments p ON pj.paymentId = p.id\r\n"
//			+ "			LEFT JOIN CashIn ci ON pj.cashInId = ci.id\r\n"
//			+ "			LEFT JOIN CashOut co ON pj.cashOutId = co.id\r\n"
//			+ "			GROUP BY date(pj.recordDate)\r\n"
//			+ "			ORDER BY date(pj.recordDate) ASC")
//			public List<Commissions_Report> getCommissionsReport();


	@Query("SELECT new com.isms.model.PaymentReceipt_Report(p.id, p.paymentDate, c.clientName, c.telNo, p.amountPaid, p.paymentProof) \r\n"
			+ "FROM Payments p \r\n" 
			+ "LEFT JOIN Clients c ON p.clients.id = c.id\r\n" 
			+ "WHERE p.id = :id")
	public List<PaymentReceipt_Report> getPaymentReceipt(@Param("id") int id);


	@Query("SELECT DISTINCT new com.isms.model.ClientList_Report(COALESCE(c.id, s.id), COALESCE(c.clientName, s.personnelName), COALESCE(c.telNo, s.telNo), COALESCE(SUM(pj.amount),0))\r\n"
			+ "			FROM PaymentJobItem pj \r\n"
			+ "			LEFT JOIN Clients c ON pj.clientId = c.id \r\n"
			+ "			LEFT JOIN Personnels s ON pj.personnelId = s.id\r\n"
			+ "			GROUP BY COALESCE(c.id, s.id) \r\n"
			+ "			ORDER BY COALESCE(c.id, s.id) ASC")
	public Page<ClientList_Report> getClientsReportForDatatable(ClientsReportDataFilter clientsReportDataFilter,
			Pageable pageable);
}
