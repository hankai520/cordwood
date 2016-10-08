
package ren.hankai.cordwood.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import ren.hankai.cordwood.core.domain.Plugin;
import ren.hankai.cordwood.persist.model.PluginBean;
import ren.hankai.cordwood.persist.util.CustomJpaRepository;

/**
 * @author hankai
 * @version TODO Missing version number
 * @since Sep 30, 2016 11:13:19 AM
 */
@Transactional
public interface PluginRepository
                extends CustomJpaRepository<Plugin>, JpaRepository<PluginBean, BigInteger> {
}
