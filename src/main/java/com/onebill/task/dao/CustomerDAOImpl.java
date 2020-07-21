package com.onebill.task.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.onebill.task.dto.Addresses;
import com.onebill.task.dto.CustomerDetails;
import com.onebill.task.dto.CustomerDocuments;
import com.onebill.task.dto.Emails;
import com.onebill.task.dto.MobileNumbers;
import com.onebill.task.exceptions.EmailAlreadyExistsException;
import com.onebill.task.exceptions.MobileNumberAlreadyExistsException;

import lombok.extern.java.Log;

@Log
@Repository
public class CustomerDAOImpl implements CustomerDAO {

	@PersistenceUnit
	private EntityManagerFactory fact;

	@Override
	public boolean createCustomer(CustomerDetails details, CustomerDocuments documents) {
		EntityManager mgr = fact.createEntityManager();
		EntityTransaction tx = mgr.getTransaction();
			details.setDocuments(documents);
			documents.setCustomer(details);
			
			List<MobileNumbers> mobileNums = getMobileNumbers();
			List<Emails> emails = getEMails();
		for (Addresses address : details.getAddresses()) {
			address.setCustomer(details);
		}
		for (MobileNumbers mobileNumber : details.getPhoneNumbers()) {
			if(mobileNumber.getMobileNumber() != null) {
			if(mobileNums.size() != 0) {
				for (MobileNumbers number : mobileNums) {
					if(number.getMobileNumber().toString().equals(mobileNumber.getMobileNumber().toString())) {
						throw new MobileNumberAlreadyExistsException();
					}
				}
			}
			}
			mobileNumber.setCustomer(details);
		}
		for (Emails email : details.getEmails()) {
			if(emails.size() != 0) {
				for (Emails customerEmail : emails) {
					if(email.getEmail().equals(customerEmail.getEmail())) {
						throw new EmailAlreadyExistsException();
					}
				}
			}
			email.setCustomer(details);
		}
		try {
			tx.begin();
			details.setStatus("active");
			mgr.persist(details);
			tx.commit();
			return true;
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return false;
			}
		}
		return false;
	}

	@Override
	public CustomerDetails updateCustomer(CustomerDetails details, CustomerDocuments documents) {
		EntityManager mgr = fact.createEntityManager();
		EntityTransaction tx = mgr.getTransaction();
		try {
			tx.begin();
			CustomerDetails bean = mgr.find(CustomerDetails.class, details.getCustomerId());
			bean.setDateOfBirth(details.getDateOfBirth());
			bean.setFirstName(details.getFirstName());
			bean.setLastName(details.getLastName());

			for (Addresses address : details.getAddresses()) {
				if (address.getAddressId() != 0) {
					Addresses addr = mgr.find(Addresses.class, address.getAddressId());
					addr.setCity(address.getCity());
					addr.setCountry(address.getCountry());
					addr.setState(address.getState());
					addr.setLine1(address.getLine1());
					addr.setLine2(address.getLine2());
					addr.setPincode(address.getPincode());
					mgr.persist(addr);
				} else {
					Addresses addr = new Addresses();
					addr.setCity(address.getCity());
					addr.setCountry(address.getCountry());
					addr.setState(address.getState());
					addr.setLine1(address.getLine1());
					addr.setLine2(address.getLine2());
					addr.setPincode(address.getPincode());
					addr.setCustomer(details);
					mgr.persist(addr);
				}
			}

			for (Emails email : details.getEmails()) {
				if (email.getEmailId() != 0) {
					Emails em = mgr.find(Emails.class, email.getEmailId());
					em.setEmail(email.getEmail());
					mgr.persist(em);
				} else {
					Emails em = new Emails();
					em.setEmail(email.getEmail());
					em.setCustomer(details);
					mgr.persist(em);
				}
			}

			for (MobileNumbers mobileNumber : details.getPhoneNumbers()) {
				if (mobileNumber.getMobileId() != 0) {
					MobileNumbers mobileNum = mgr.find(MobileNumbers.class, mobileNumber.getMobileId());
					mobileNum.setMobileNumber(mobileNumber.getMobileNumber());
				} else {
					MobileNumbers mobileNum = new MobileNumbers();
					mobileNum.setMobileNumber(mobileNumber.getMobileNumber());
					mobileNum.setCustomer(details);
					mgr.persist(mobileNum);
				}
			}

			if (documents.getAddressProof() != null || documents.getIdentityProof() != null) {
				@SuppressWarnings("unchecked")
				TypedQuery<CustomerDocuments> query = (TypedQuery<CustomerDocuments>) mgr.createNativeQuery("select * from documents_info where customerId="+details.getCustomerId(), 
						CustomerDocuments.class);
				CustomerDocuments document = query.getSingleResult();
				if (document != null) {
					if(documents.getAddressProof() != null) {
						System.out.println(documents.getAddressProofFileName());
					document.setAddressProof(documents.getAddressProof());
					document.setAddressProofFileName(documents.getAddressProofFileName());
					}
					if(documents.getIdentityProof() != null) {
						System.out.println(documents.getIdentityProofFileName());
					document.setIdentityProof(documents.getIdentityProof());
					document.setIdentityProofFileName(documents.getIdentityProofFileName());
					}
					mgr.persist(document);
				}
			}

			mgr.persist(bean);
			tx.commit();
			return details;
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return null;
			}
		}
		return null;
	}

	@Override
	public List<CustomerDetails> getCustomers() {
		EntityManager mgr = fact.createEntityManager();
		try {
			String jpql = "select m from CustomerDetails m where m.status = 'active'";
			TypedQuery<CustomerDetails> query = mgr.createQuery(jpql, CustomerDetails.class);
			List<CustomerDetails> beans = query.getResultList();
			if (beans != null) {
				for (int i = 0; i < beans.size(); i++) {
					if (beans.get(i).getDocuments() != null) {
						CustomerDocuments documents = new CustomerDocuments();
						documents.setAddressProofFileName(beans.get(i).getDocuments().getAddressProofFileName());
						documents.setIdentityProofFileName(beans.get(i).getDocuments().getIdentityProofFileName());
						documents.setDocumentId(beans.get(i).getDocuments().getDocumentId());
						beans.get(i).setDocuments(documents);
					}
				}
				return beans;
			} else {
				return null;
			}
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		EntityManager mgr = fact.createEntityManager();
		EntityTransaction tx = mgr.getTransaction();
		try {
			tx.begin();
			CustomerDetails bean = mgr.find(CustomerDetails.class, customerId);
			bean.setStatus("inactive");
			mgr.persist(bean);
			tx.commit();
			return true;
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return false;
			}
		}
		return false;
	}
	
	@Override
	public CustomerDetails getCustomer(String email) {
		EntityManager mgr = fact.createEntityManager();
		try {
			String jpql = "select customerId from emails_info where email="+email;
			Query query = mgr.createNativeQuery(jpql);
			int customerId = query.getFirstResult();
			System.out.println(customerId);
			CustomerDetails bean = mgr.find(CustomerDetails.class, customerId);
			if(bean != null) {
				return bean;
			} else {
				return null;
			}
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return null;
			}
		}
		return null;
	}
	
	List<Emails> getEMails() {
		String emailQuery = "select e from Emails e";
		EntityManager mgr = fact.createEntityManager();
		TypedQuery<Emails> eQuery  = mgr.createQuery(emailQuery, Emails.class);
		try {
		List<Emails> emails = eQuery.getResultList();	
		return emails;
		}catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return null;
			}
		}
		return null;
	}
	
	List<MobileNumbers> getMobileNumbers() {
		String mobileQuery = "select e from MobileNumbers e";
		EntityManager mgr = fact.createEntityManager();
		TypedQuery<MobileNumbers> mQuery = mgr.createQuery(mobileQuery, MobileNumbers.class);
		try {
		List<MobileNumbers> mobileNums = mQuery.getResultList();
		return mobileNums;
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				log.info(ele.toString());
				return null;
			}
		}
		return null;
	}
}
