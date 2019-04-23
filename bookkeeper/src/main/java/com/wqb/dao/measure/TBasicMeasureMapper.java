package com.wqb.dao.measure;

import com.wqb.model.TBasicMeasure;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 司氏旭东
 * @ClassName: TBasicMeasureMapper
 * @Description: 数量单位数据持久层接口
 * @date 2017年12月20日 上午10:04:55
 */
@Component
public interface TBasicMeasureMapper {

    int deleteByPrimaryKey(String pkMeasureId);

    int insertMeasure(TBasicMeasure record);

    List<TBasicMeasure> queryMeasure(TBasicMeasure record);

    int updateMeasure(TBasicMeasure tBasicMeasure);

    List<TBasicMeasure> queryMeasureBySymbolOrName(TBasicMeasure tBasicMeasure);

}
