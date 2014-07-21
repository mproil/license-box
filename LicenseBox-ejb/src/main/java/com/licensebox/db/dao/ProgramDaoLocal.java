package com.licensebox.db.dao;

import com.licensebox.db.entity.Program;
import java.util.List;
import javax.ejb.Local;

/**
 * This Interface defines the methods that will be used in the Program DAO
 *
 * @author Anna Guzman & Michael Paltsev
 */
@Local
public interface ProgramDaoLocal {

    /**
     * Adds a new Program to the database
     * @param program The program that you want to add
     */
    void createProgram(Program program);

    /**
     * Updates a Program in the database
     * @param program The Program that you want to update
     */
    void editProgram (Program program);

    /**
     * Finds a Program by the Program Id
     * @param programId The program id that you want to search for
     * @return A Program or a null object if none is found
     */
    Program getByProgramId(Integer programId);

    /**
     * Finds all the Programs in the database
     * @return A List of Program objects or null if none found
     */
    List<Program> getAll();
       
}