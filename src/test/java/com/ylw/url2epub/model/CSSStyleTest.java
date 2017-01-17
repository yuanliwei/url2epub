package com.ylw.url2epub.model;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.http.util.TextUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ylw.url2epub.utils.FileUtil;

import cz.vutbr.web.css.CSSException;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.RuleBlock;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;

public class CSSStyleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse() throws IOException, CSSException {
		String cssString = FileUtil
				.getString("C:\\Users\\ylw\\Desktop\\new epub\\xxx\\3e1053747b46e8b510fa21821a5ab0a1.css");
		StrBuilder sb = new StrBuilder();
		StrBuilder tem = new StrBuilder();

		StyleSheet css = CSSFactory.parseString(cssString, null);
		List<RuleBlock<?>> cList = css.asList();
		// RuleBlock<?> block = cList.get(0);
		for (RuleBlock<?> block : cList) {
			if (block instanceof RuleSet) {
				tem.clear();
				RuleSet ruleSet = (RuleSet) block;
				CombinedSelector[] selectors = ruleSet.getSelectors();
				for (int i = 0; i < selectors.length; i++) {
					if (i > 0)
						tem.append(",");
					tem.append(selectors[i]);
				}
				tem.append("{");
				boolean hasSub = false;
				List<Declaration> declarations = ruleSet.asList();

				String templ = "background,border,color,display,float,font,height,line-height,margin,padding,text,vertical-align,white-space,width,word-break,z-index,";

				for (Declaration declaration : declarations) {
					String prop = declaration.getProperty().toLowerCase();
					String shortProp = prop.split("-")[0];
					if (TextUtils.isBlank(shortProp))
						continue;
					if (templ.contains(prop + ",") || templ.contains(shortProp + ",")) {
						hasSub = true;
						tem.append(declaration.toString());
						System.out.println(declaration.getProperty() + "----------" + declaration.toString());
					}
				}
				tem.append("}\n");
				if (hasSub) {
					sb.append(tem.toString());
				}
			}
		}
		System.out.println(cList.size());
		FileUtil.saveFullPathFile("C:\\Users\\ylw\\Desktop\\new epub\\xxx\\result.css", sb.build());
	}

}
