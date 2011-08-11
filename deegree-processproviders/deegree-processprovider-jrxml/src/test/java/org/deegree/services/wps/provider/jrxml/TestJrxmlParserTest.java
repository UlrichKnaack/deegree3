//$HeadURL: svn+ssh://lbuesching@svn.wald.intevation.de/deegree/base/trunk/resources/eclipse/files_template.xml $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.services.wps.provider.jrxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.deegree.commons.xml.XMLAdapter;
import org.deegree.process.jaxb.java.ProcessDefinition;
import org.deegree.process.jaxb.java.ProcessDefinition.InputParameters;
import org.deegree.process.jaxb.java.ProcessletInputDefinition;
import org.deegree.services.wps.provider.jrxml.contentprovider.JrxmlContentProvider;
import org.deegree.services.wps.provider.jrxml.contentprovider.SimpleContentProvider;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz</a>
 * @author last edited by: $Author: lyn $
 * 
 * @version $Revision: $, $Date: $
 */
public class TestJrxmlParserTest {

    /**
     * Test method for {@link org.deegree.services.wps.provider.jrxml.JrxmlParser#parse()}.
     */
    @Test
    public void testParse() {
        XMLAdapter jrxmlAdapter = new XMLAdapter(
                                                  TestJrxmlParserTest.class.getResourceAsStream( "testWPSreportTemplate.jrxml" ) );
        List<JrxmlContentProvider> contentProviders = new ArrayList<JrxmlContentProvider>();
        contentProviders.add( new SimpleContentProvider() );
        JrxmlParser p = new JrxmlParser();
        ProcessDefinition pd = p.parse( "processId", "testWPSreportTemplate", jrxmlAdapter, contentProviders );
        assertNotNull( pd );
        assertNotNull( pd.getIdentifier() );
        assertEquals( "processId", pd.getIdentifier().getValue() );
        assertEquals( "createReportByAWPSProcess", pd.getTitle().getValue() );

        InputParameters inputParameters = pd.getInputParameters();
        assertNotNull( inputParameters );

        List<JAXBElement<? extends ProcessletInputDefinition>> processInput = inputParameters.getProcessInput();
        assertNotNull( processInput );
        assertEquals( 8, processInput.size() );
    }

}
