/**
 *  Copyright 2013 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of OT module.
 *
 *  OT module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  OT module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OT module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.OT.db.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.OT.db.OperationTheatreDAO;
import org.openmrs.module.OT.model.MajorOTProcedure;
import org.openmrs.module.OT.model.MinorOTProcedure;
import org.openmrs.module.OT.util.OTConstants;
import org.openmrs.module.hospitalcore.model.OpdTestOrder;

/**
 * It is a default implementation of  {@link OperationTheatreDAO}.
 */
public class HibernateOperationTheatreDAO implements OperationTheatreDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

	@SuppressWarnings("unchecked")
	public List<OpdTestOrder> getSchedulesMinorOT(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, int page, String phrase) 
					throws ParseException{
		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return Collections.EMPTY_LIST;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					OpdTestOrder.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("scheduleDate",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.eq("billingStatus", 1));
			criteria.add(Restrictions.eq("cancelStatus", 0));
			criteria.add(Restrictions.in("valueCoded", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			criteria.addOrder(org.hibernate.criterion.Order.asc("scheduleDate"));
			int firstResult = (page - 1) * OTConstants.PAGESIZE;
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(OTConstants.PAGESIZE);
			return criteria.list();
		}
	}
	
	//
	// Schedule
	//
	public Integer countScheduleMinorOT(Date scheduleDate, List<Concept> procedures,
			List<Patient> patients, String phrase) throws ParseException {
		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return 0;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					OpdTestOrder.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("scheduleDate",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.eq("billingStatus", 1));
			criteria.add(Restrictions.eq("cancelStatus", 0));
			criteria.add(Restrictions.in("valueCoded", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			Number rs = (Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult();
			return rs != null ? rs.intValue() : 0;
		}
	}

	public List<Obs> getObsInstanceForDiagnosis(Encounter encounter,Concept concept,Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria
				(Obs.class);
		criteria.add(Restrictions.eq("encounter", encounter));
		criteria.add(Restrictions.eq("concept", concept));
		criteria.add(Restrictions.eq("dateCreated", date));
		return criteria.list();
	}
	
	public Obs getObsInstanceForProcedure(Encounter encounter,Concept valuCoded,Date date) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria
				(Obs.class);
		criteria.add(Restrictions.eq("encounter", encounter));
		criteria.add(Restrictions.eq("valueCoded", valuCoded));
		criteria.add(Restrictions.eq("dateCreated", date));
		return (Obs) criteria.uniqueResult();
	}

	public OpdTestOrder getAcceptedSchedule(Integer orderId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria
				(OpdTestOrder.class);
		criteria.add(Restrictions.eq("opdOrderId", orderId));
		return (OpdTestOrder) criteria.uniqueResult();
	}

	public MinorOTProcedure getMinorOTProcedure(OpdTestOrder opdOrderId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria
				(MinorOTProcedure.class);
		criteria.add(Restrictions.eq("opdOrderId", opdOrderId));
		return (MinorOTProcedure) criteria.uniqueResult();
	}

	public MinorOTProcedure saveOTProcedure(MinorOTProcedure procedure) {
		return (MinorOTProcedure) sessionFactory.getCurrentSession().merge(procedure);
	}

	@SuppressWarnings("unchecked")
	public List<MinorOTProcedure> getMinorOTSchedules(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, Integer page, String phrase) throws ParseException {
		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return Collections.EMPTY_LIST;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					MinorOTProcedure.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("otSchedule",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.in("procedure", procedures));
			criteria.add(Restrictions.ne("status", OTConstants.PROCEDURE_STATUS_COMPLETED));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			criteria.addOrder(org.hibernate.criterion.Order.asc("otSchedule"));
			int firstResult = (page - 1) * OTConstants.PAGESIZE;
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(OTConstants.PAGESIZE);
			return criteria.list();
		}
	}

	public Integer countMinorOTSchedule(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, String phrase)
			throws ParseException {
		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return 0;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					MinorOTProcedure.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("otSchedule",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.in("procedure", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			Number rs = (Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult();
			return rs != null ? rs.intValue() : 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OpdTestOrder> getSchedulesMajorOT(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, int page,
			String phrase) throws ParseException {

		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return Collections.EMPTY_LIST;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					OpdTestOrder.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("scheduleDate",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			//Criterion billingStatus = Restrictions.eq("billingStatus", 1);
			//Criterion indoorStatus = Restrictions.eq("indoorStatus", 1);
			// To get records matching with OR condistions
			//LogicalExpression orExp = Restrictions.or(billingStatus, indoorStatus);
			//criteria.add( orExp );
			criteria.add(Restrictions.eq("billingStatus", 1));
			criteria.add(Restrictions.eq("cancelStatus", 0));
			criteria.add(Restrictions.in("valueCoded", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			criteria.addOrder(org.hibernate.criterion.Order.asc("scheduleDate"));
			int firstResult = (page - 1) * OTConstants.PAGESIZE;
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(OTConstants.PAGESIZE);
			return criteria.list();
		}
	}

	public Integer countScheduleMajorOT(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, String phrase)
			throws ParseException {
		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return 0;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					OpdTestOrder.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("scheduleDate",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			//Criterion billingStatus = Restrictions.eq("billingStatus", 1);
			//Criterion indoorStatus = Restrictions.eq("indoorStatus", 1);
			// To get records matching with OR condistions
			//LogicalExpression orExp = Restrictions.or(billingStatus, indoorStatus);
			//criteria.add( orExp );
			criteria.add(Restrictions.eq("billingStatus", 1));
			criteria.add(Restrictions.eq("cancelStatus", 0));
			criteria.add(Restrictions.in("valueCoded", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			Number rs = (Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult();
			return rs != null ? rs.intValue() : 0;
		}
	}

	public MajorOTProcedure getMajorOTProcedure(OpdTestOrder opdOrderId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria
				(MajorOTProcedure.class);
		criteria.add(Restrictions.eq("opdOrderId", opdOrderId));
		return (MajorOTProcedure) criteria.uniqueResult();
	}

	public MajorOTProcedure saveOTProcedure(MajorOTProcedure procedure) {
		return (MajorOTProcedure) sessionFactory.getCurrentSession().merge(procedure);
	}

	@SuppressWarnings("unchecked")
	public List<MajorOTProcedure> getMajorOTSchedules(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, Integer page,
			String phrase) throws ParseException {

		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return Collections.EMPTY_LIST;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					MajorOTProcedure.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("otSchedule",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.in("procedure", procedures));
			criteria.add(Restrictions.ne("status", OTConstants.PROCEDURE_STATUS_COMPLETED));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			criteria.addOrder(org.hibernate.criterion.Order.asc("otSchedule"));
			int firstResult = (page - 1) * OTConstants.PAGESIZE;
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(OTConstants.PAGESIZE);
			return criteria.list();
		}
	}

	public Integer countMajorOTSchedule(Date scheduleDate,
			List<Concept> procedures, List<Patient> patients, String phrase)
			throws ParseException {

		if (CollectionUtils.isEmpty(patients) && !phrase.equals(""))
			return 0;
		else {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
					MajorOTProcedure.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = sdf.format(scheduleDate) + " 00:00:00";
			String endDate = sdf.format(scheduleDate) + " 23:59:59";
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			criteria.add(Expression.between("otSchedule",
					dateTimeFormatter.parse(startDate),
					dateTimeFormatter.parse(endDate)));
			criteria.add(Restrictions.in("procedure", procedures));
			if (!CollectionUtils.isEmpty(patients))
				criteria.add(Restrictions.in("patient", patients));
			Number rs = (Number) criteria.setProjection(Projections.rowCount())
					.uniqueResult();
			return rs != null ? rs.intValue() : 0;
		}
	}
}