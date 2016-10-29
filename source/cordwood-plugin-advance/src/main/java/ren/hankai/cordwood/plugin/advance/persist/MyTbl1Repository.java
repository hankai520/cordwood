package ren.hankai.cordwood.plugin.advance.persist;

import org.springframework.data.jpa.repository.JpaRepository;

import ren.hankai.cordwood.plugin.advance.model.MyTbl1;


/**
 * 数据仓库。
 *
 * @author hankai
 * @version 1.0.0
 * @since Oct 9, 2016 4:34:49 PM
 */
public interface MyTbl1Repository extends JpaRepository<MyTbl1, Integer> {
}
