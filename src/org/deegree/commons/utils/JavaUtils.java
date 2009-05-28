//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 ---------------------------------------------------------------------------*/

package org.deegree.commons.utils;

import static java.lang.reflect.Modifier.STATIC;
import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.Field;

import org.slf4j.Logger;

/**
 * <code>JavaUtils</code>
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class JavaUtils {

    private static final Logger LOG = getLogger( JavaUtils.class );

    /**
     * @param o
     * @return a string describing the object's fields' values
     */
    public static String generateToString( Object o ) {
        if ( o == null ) {
            return null;
        }

        Class<?> c = o.getClass();
        StringBuffer sb = new StringBuffer( c.getSimpleName() ).append( "[" );
        do {
            for ( Field f : c.getDeclaredFields() ) {
                Class<?> t = f.getType();
                try {
                    if ( ( f.getModifiers() & STATIC ) != 0 ) {
                        continue;
                    }
                    f.setAccessible( true );
                    if ( t.isPrimitive() ) {
                        sb.append( f.getName() ).append( ": " ).append( f.get( o ) ).append( ", " );
                    } else {
                        String s = generateToString( f.get( o ) );
                        if ( s != null ) {
                            sb.append( f.getName() ).append( ": " ).append( s ).append( ", " );
                        }
                    }
                } catch ( IllegalArgumentException e ) {
                    LOG.debug( "Stack trace while trying to output an object: ", e );
                } catch ( IllegalAccessException e ) {
                    LOG.debug( "Stack trace while trying to output an object: ", e );
                }
            }
        } while ( ( c = c.getSuperclass() ) != null );

        return sb.append( "]" ).toString();
    }

}
