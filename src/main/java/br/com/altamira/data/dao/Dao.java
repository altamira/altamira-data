///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.com.altamira.data.dao;
//
//import static br.com.altamira.data.dao.manufacturing.bom.BOMDao.ENTITY_VALIDATION;
//import static br.com.altamira.data.dao.manufacturing.bom.BOMDao.NUMBER_VALIDATION;
//import static br.com.altamira.data.dao.manufacturing.bom.BOMDao.PAGE_SIZE_VALIDATION;
//import static br.com.altamira.data.dao.manufacturing.bom.BOMDao.START_PAGE_VALIDATION;
//import java.util.List;
//import javax.persistence.NoResultException;
//import javax.validation.ConstraintViolationException;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//
///**
// *
// * @author Alessandro
// * @param <T>
// */
//public interface Dao<T> {
//    
//    public List<T> list(
//            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
//            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
//            throws ConstraintViolationException;
//    
//    /*public List<T> search (
//            @NotNull @Size(min = 2, message = SEARCH_VALIDATION) String search,
//            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
//            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
//        throws ConstraintViolationException, NoResultException;*/
//    
//    public T findById(
//            @Min(value = 1, message = NUMBER_VALIDATION) long id) 
//            throws ConstraintViolationException, NoResultException;
//    
//    public T create(
//            @NotNull(message = ENTITY_VALIDATION) T entity) 
//            throws ConstraintViolationException;
//    
//    public T update(
//            @NotNull(message = ENTITY_VALIDATION) T entity)
//            throws ConstraintViolationException, IllegalArgumentException;
//    
//    public void remove(
//            @NotNull(message = ENTITY_VALIDATION) T entity) 
//            throws ConstraintViolationException, IllegalArgumentException;
//    
//    public void remove(
//            @Min(value = 1, message = NUMBER_VALIDATION) long id) 
//            throws ConstraintViolationException, NoResultException;
//    
//}
