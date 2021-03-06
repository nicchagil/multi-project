package com.nicchagil.util.excel.parse.controller.demo;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.locale.converters.IntegerLocaleConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nicchagil.util.Map2BeanUtils;
import com.nicchagil.util.excel.configvo.CellIndex;
import com.nicchagil.util.excel.parse.ExcelParseConfigVo;
import com.nicchagil.util.excel.parse.WorkBookParseUtils;
import com.nicchagil.util.fileupload.MultipartFileAssert;

@RestController
@RequestMapping("/test/excel")
public class ImportParseController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping("/import")
    public void fileUpload(@RequestParam("file") MultipartFile multipartFile) {
		MultipartFileAssert.notEmpty(multipartFile);
		MultipartFileAssert.sizeLimit(multipartFile, 5 * 1024 * 1024 * 1024, "请上传小于5M的文件");
		
		InputStream is = null;
		try {
			is = multipartFile.getInputStream();
			
			Workbook wb = WorkbookFactory.create(is);
			
			ExcelParseConfigVo configVo = new ExcelParseConfigVo();
			configVo.setBatchDataColumnKey(new String[] {"id", "name", "sex", "birthday"});
			configVo.setBatchDataListStartCellIndex(new CellIndex(2, 0));
			
			List<Map<String, String>> dataList = WorkBookParseUtils.getBatchData(wb, configVo);
			logger.info("Upload dataList : {}", dataList);
			
			/* Date转换器 */
			ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
			convertUtilsBean.register(new DateConverter(), Date.class);
			convertUtilsBean.register(new IntegerLocaleConverter(), Integer.class); // 处理NULL的情况
			
			/* 转换为指定Bean的集合 */
			List<User> userList = Map2BeanUtils.mapList2BeanListByConverters(User.class, dataList, convertUtilsBean);
			logger.info("Upload userList : {}", userList);
			
		} catch (Exception e) {
			throw new RuntimeException("读取文件异常", e);
		}
    }
	
	public static class User {
		private Integer id;
		private String name;
		private String sex;
		private Date birthday;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", sex=" + sex + ", birthday=" + birthday + "]";
		}
	}

}
