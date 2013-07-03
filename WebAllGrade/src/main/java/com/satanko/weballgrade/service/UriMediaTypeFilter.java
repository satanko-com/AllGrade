/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service;

import com.sun.jersey.api.container.filter.UriConnegFilter;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

/**
 * The class maps url extensions to {@link MediaType} objects.
 * <p>
 * External links:<br>
 * <a href=http://goo.gl/hnLPf>http://goo.gl/hnLPf</a> (stackoverflow.com)<br>
 * <a href=http://zcox.wordpress.com/2009/08/11/uri-extensions-in-jersey/>http://zcox.wordpress.com/2009/08/11/uri-extensions-in-jersey/</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class UriMediaTypeFilter extends UriConnegFilter {
    
 private static final Map<String, MediaType> mappedMediaTypes = new HashMap<>();

  static {
    mappedMediaTypes.put("json", MediaType.APPLICATION_JSON_TYPE);
  }

  public UriMediaTypeFilter() {
    super(mappedMediaTypes);
  }
}
