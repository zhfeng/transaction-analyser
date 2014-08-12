/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package io.narayana.nta.restapi.test.integration;

import io.narayana.nta.restapi.services.TraceLoggingService;
import io.narayana.nta.restapi.services.TraceLoggingServiceImpl;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * @author <a href="mailto:zfeng@redhat.com">Amos Feng</a>
 */
@WarpTest
@RunWith(Arquillian.class)
public class RESTServiceTest {
    @ArquillianResource
    private URL contextPath;

    private TraceLoggingService traceLoggingService;

    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive createDeployment() {
        String ManifestMF = "Manifest-Version: 1.0\n"
                + "Dependencies: org.jboss.jts, org.jboss.as.controller-client, org.jboss.dmr\n";

        File[] libs = Maven.resolver()
                .loadPomFromFile("../core/pom.xml").resolve("commons-io:commons-io")
                .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class, "restapi.war")
                .addPackages(true, "io.narayana.nta")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/test/resources/persistence.xml")),
                        "classes/META-INF/persistence.xml")
                .addAsManifestResource(new FileAsset(new File("src/test/resources/nta-test-ds.xml")), "nta-test-ds.xml")
                .addAsLibraries(libs)
                .setManifest(new StringAsset(ManifestMF));
    }

    @BeforeClass
    public static void setUpClass() {

        // initializes the rest easy client framework
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    @Before
    public void setUp() {

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget  target = client.target(contextPath + "rest");

        traceLoggingService = target.proxy(TraceLoggingServiceImpl.class);
    }

    @Test
    @RunAsClient
    public void testTraceLoggingGetWarp() {
        Warp.initiate(new Activity() {
            @Override
            public void perform() {
                boolean response = traceLoggingService.getTraceLoggingEnable();
                assertEquals("getTraceStatus returned incorrect status code ", false, response);
            }
        }).inspect(new Inspection() {

        });
    }
}
