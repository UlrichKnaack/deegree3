//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
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
package org.deegree.metadata.persistence;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

import org.deegree.metadata.MetadataRecord;
import org.deegree.protocol.csw.CSWConstants.ReturnableElement;

/**
 * Base interface of the {@link MetadataStore} persistence layer, provides access to stored {@link MetadataStore}
 * instances and their schemas.
 * <p>
 * NOTE: One {@link MetadataStore} instance corresponds to one metadata format (e.g. DublinCore, MD_Metadata (ISO
 * TC211), SV_Service (ISO TC211)).
 * </p>
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface MetadataStore {

    /**
     * Called by the container to indicate that this {@link MetadataStore} instance is being placed into service.
     * 
     * @throws MetadataStoreException
     *             if the initialization fails
     */
    public void init()
                            throws MetadataStoreException;

    /**
     * Called by the container to indicate that this {@link MetadataStore} instance is being taken out of service.
     */
    public void destroy();

    /**
     * Exports the XML schema for the associated metadata format.
     * 
     * @param writer
     *            writer to export to, must not be <code>null</code>
     * @param typeName
     *            specifies which record profile should be returned in the response.
     */
    public void describeRecord( XMLStreamWriter writer, QName typeName );

    /**
     * 
     * Exports the XML for the requested records.
     * 
     * @param writer
     *            writer to export to, must not be <code>null</code>
     * @param typeName
     *            of a specific requested record profile
     * @param outputSchema
     *            that should present in the response. If there is a DC recordStore requested and the outputSchema is a
     *            ISO schema then there should be presented the ISO representation of the record.
     * @param recordStoreOptions
     *            {@link RecordStoreOptions}
     * @throws MetadataStoreException
     */
    public List<MetadataRecord> getRecords( QName typeName, URI outputSchema, RecordStoreOptions recordStoreOptions )
                            throws MetadataStoreException;

    /**
     * Exports the records by the requested identifier.
     * 
     * @param writer
     *            writer to export to, must not be <code>null</code>
     * @param idList
     *            list of the requested identifiers
     * @param outputSchema
     *            that should be presented in the response
     * @param elementSetName
     *            {@link ReturnableElement}
     * @throws MetadataStoreException
     */
    public List<MetadataRecord> getRecordById( List<String> idList, URI outputSchema, ReturnableElement elementSetName )
                            throws MetadataStoreException;

    /**
     * Acquires transactional access to the metadata store.
     * 
     * @return transaction object that allows to perform transactions operations on the metadata store, never
     *         <code>null</code>
     * @throws MetadataStoreException
     *             if the transactional access could not be acquired or is not implemented for this
     *             {@link MetadataStore}
     */
    public MetadataStoreTransaction acquireTransaction()
                            throws MetadataStoreException;

    /**
     * Returns the typeNames that are known in the backend. <br>
     * i.e. the ISORecordStore holds two profiles, the DUBLIN CORE and the ISO profile.
     * 
     * @return a map from a QName to an int value
     * 
     */
    public Map<QName, Integer> getTypeNames();

}
