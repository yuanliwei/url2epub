package com.ylw.url2epub.model;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.text.StrBuilder;
import org.apache.http.util.TextUtils;

import cz.vutbr.web.css.CSSException;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.RuleBlock;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;

public class CSSStyle {
	static String templ = "background,border,color,display,float,font,height,line-height,margin,padding,text,vertical-align,white-space,width,word-break,z-index,";

	public static String simple(String style) {
		StrBuilder sb = new StrBuilder();
		StrBuilder tem = new StrBuilder();

		StyleSheet css;
		try {
			css = CSSFactory.parseString(style, null);
		} catch (IOException | CSSException e) {
			return "";
		}
		List<RuleBlock<?>> cList = css.asList();
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

				for (Declaration declaration : declarations) {
					String prop = declaration.getProperty().toLowerCase();
					String shortProp = prop.split("-")[0];
					if (TextUtils.isBlank(shortProp))
						continue;
					if (templ.contains(prop + ",") || templ.contains(shortProp + ",")) {
						hasSub = true;
						tem.append(declaration.toString());
					}
				}
				tem.append("}");
				if (hasSub) {
					sb.append(tem.toString());
				}
			}
		}
		return sb.build();
	}
}
