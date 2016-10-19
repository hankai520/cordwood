package com.demo.plugins.persist;

import com.demo.plugins.persist.model.MyTbl1;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author hankai
 * @version TODO Missing version number
 * @since Oct 9, 2016 4:34:49 PM
 */
public interface MyTbl1Repository extends JpaRepository<MyTbl1, Integer> {
}
