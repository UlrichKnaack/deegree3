//$HeadURL$
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2009 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth  
 lat/lon GmbH 
 Aennchenstr. 19
 53115 Bonn
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
package org.deegree.geometry.standard.primitive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.deegree.crs.CRS;
import org.deegree.geometry.linearization.CurveLinearizer;
import org.deegree.geometry.linearization.LinearizationCriterion;
import org.deegree.geometry.linearization.NumPointsCriterion;
import org.deegree.geometry.primitive.Curve;
import org.deegree.geometry.primitive.LineString;
import org.deegree.geometry.primitive.Point;
import org.deegree.geometry.primitive.curvesegments.CurveSegment;
import org.deegree.geometry.primitive.curvesegments.LineStringSegment;
import org.deegree.geometry.primitive.curvesegments.CurveSegment.CurveSegmentType;
import org.deegree.geometry.standard.AbstractDefaultGeometry;
import org.deegree.geometry.standard.DefaultGeometryFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Default implementation of {@link Curve}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author:$
 * 
 * @version $Revision:$, $Date:$
 */
public class DefaultCurve extends AbstractDefaultGeometry implements Curve {

    private List<CurveSegment> segments;

    /**
     * Creates a new {@link DefaultCurve} instance from the given parameters.
     * 
     * @param id
     *            identifier of the created geometry object
     * @param crs
     *            coordinate reference system
     * @param segments
     *            segments that constitute the curve
     */
    public DefaultCurve( String id, CRS crs, List<CurveSegment> segments ) {
        super( id, crs );
        this.segments = new ArrayList<CurveSegment>( segments );
    }

    @Override
    public List<Point> getBoundary() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CurveSegment> getCurveSegments() {
        return segments;
    }

    @Override
    public double getLength() {
        return ( (com.vividsolutions.jts.geom.LineString) getJTSGeometry() ).getLength();
    }

    @Override
    public boolean isClosed() {
        return getStartPoint().equals( getEndPoint() );
    }

    @Override
    public LineString getAsLineString() {
        if ( segments.size() != 1 && !( segments.get( 0 ) instanceof LineString ) ) {
            throw new UnsupportedOperationException();
        }
        return new DefaultLineString( null, getCoordinateSystem(),
                                      ( (LineStringSegment) segments.get( 0 ) ).getControlPoints() );
    }

    @Override
    public CurveType getCurveType() {
        return CurveType.Curve;
    }

    @Override
    public Point getStartPoint() {
        return segments.get( 0 ).getStartPoint();
    }

    @Override
    public Point getEndPoint() {
        return segments.get( segments.size() - 1 ).getEndPoint();
    }

    @Override
    public PrimitiveType getPrimitiveType() {
        return PrimitiveType.Curve;
    }

    @Override
    public GeometryType getGeometryType() {
        return GeometryType.PRIMITIVE_GEOMETRY;
    }

    @Override
    public List<Point> getControlPoints() {
        List<Point> controlPoints = new ArrayList<Point>();
        for ( CurveSegment segment : segments ) {
            if ( segment.getSegmentType() == CurveSegmentType.LINE_STRING_SEGMENT ) {
                controlPoints.addAll( ( (LineStringSegment) segment ).getControlPoints() );
            } else {
                String msg = "Cannot determine control points for curve, contains non-linear segments.";
                throw new IllegalArgumentException( msg );
            }
        }
        return controlPoints;
    }

    @Override
    protected com.vividsolutions.jts.geom.Geometry buildJTSGeometry() {
        CurveLinearizer linearizer = new CurveLinearizer( new DefaultGeometryFactory() );
        // TODO how to determine a feasible linearization criterion?
        LinearizationCriterion crit = new NumPointsCriterion( 100 );
        List<Coordinate> coords = new LinkedList<Coordinate>();
        for ( CurveSegment segment : segments ) {
            LineStringSegment lsSegment = linearizer.linearize( segment, crit );
            coords.addAll( getCoordinates( lsSegment ) );
        }
        return jtsFactory.createLineString( coords.toArray( new Coordinate[coords.size()] ) );
    }
    
    private Collection<Coordinate> getCoordinates( LineStringSegment lsSegment ) {
        List<Point> points = lsSegment.getControlPoints();
        List<Coordinate> coordinates = new ArrayList<Coordinate>( points.size() );
        for ( Point point : points ) {
            coordinates.add( new Coordinate( point.getX(), point.getY(), point.getZ() ) );
        }
        return coordinates;
    }    
}
