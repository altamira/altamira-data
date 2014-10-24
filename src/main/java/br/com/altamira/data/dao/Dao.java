/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.dao;

import java.util.List;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Alessandro
 * @param <T>
 */
public interface Dao<T extends br.com.altamira.data.model.Entity> {
    
    public static final String START_PAGE_VALIDATION = "Invalid start page number, must be greater than 0.";
    public static final String PAGE_SIZE_VALIDATION = "Invalid page size, must be greater than 0.";
    public static final String NUMBER_VALIDATION = "Invalid number, must be greater than zero.";
    public static final String ENTITY_VALIDATION = "Entity can't be null.";
    public static final String ID_NULL_VALIDATION = "Entity id must be null or zero.";
    public static final String ID_NOT_NULL_VALIDATION = "Entity id can't be null or zero.";
    public static final String SEARCH_VALIDATION = "Search word can't be null and size must be greater that 2 characters.";

    public List<T> list(
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
            throws ConstraintViolationException;
    
    /*public List<T> search (
            @NotNull @Size(min = 2, message = SEARCH_VALIDATION) String search,
            @Min(value = 0, message = START_PAGE_VALIDATION) int startPage, 
            @Min(value = 1, message = PAGE_SIZE_VALIDATION) int pageSize) 
        throws ConstraintViolationException, NoResultException;*/
    
    public T find(
            @Min(value = 1, message = NUMBER_VALIDATION) long id) 
            throws ConstraintViolationException, NoResultException;
    
    public T create(
            @NotNull(message = ENTITY_VALIDATION) T entity) 
            throws ConstraintViolationException;
    
    public T update(
            @NotNull(message = ENTITY_VALIDATION) T entity)
            throws ConstraintViolationException, IllegalArgumentException;
    
    public void remove(
            @NotNull(message = ENTITY_VALIDATION) T entity) 
            throws ConstraintViolationException, IllegalArgumentException;
    
    public void remove(
            @Min(value = 1, message = NUMBER_VALIDATION) long id) 
            throws ConstraintViolationException, NoResultException;
    
}
