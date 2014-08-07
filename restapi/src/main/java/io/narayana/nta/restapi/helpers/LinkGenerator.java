/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package io.narayana.nta.restapi.helpers;

import io.narayana.nta.restapi.models.URIConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author Palahepitiya Gamage Amila Prabandhika &lt;amila_fiz@hotmail.com$gt;
 * Date: 14/05/14
 * Time: 00:14
 */
public final class LinkGenerator {
    public static String participantRecordURI(Long id) {
        return URIConstants.RootURI + URIConstants.ParticipantRecordURI + "/" + id;
    }

    public static String eventURI(Long id) {
        return URIConstants.RootURI + URIConstants.EventURI + "/" + id;
    }

    public static String transactionURI(Long id) {
        return URIConstants.RootURI + URIConstants.TransactionURI + "/" + id;
    }

    public static String resourceManagerURI(String branchId) {
        try{
            return URIConstants.RootURI + URIConstants.ResourceManagerURI + "/" + URLEncoder.encode(branchId,"UTF-8");
        }
        catch(UnsupportedEncodingException ex){
            return "";
        }

    }
}
