/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.yum.internal.capabilities;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.sonatype.nexus.yum.Yum;
import com.google.common.collect.Maps;

/**
 * Configuration adapter for {@link GenerateMetadataCapability}.
 *
 * @since 3.0
 */
public class GenerateMetadataCapabilityConfiguration
    extends MetadataCapabilityConfigurationSupport
{

    public static final String ALIASES = "aliases";

    public static final String DELETE_PROCESSING = "deleteProcessing";

    public static final String DELETE_PROCESSING_DELAY = "deleteProcessingDelay";

    private Map<String, String> aliases;

    private boolean processDeletes;

    private long deleteProcessingDelay;

    public GenerateMetadataCapabilityConfiguration( final String repository,
                                                    final Map<String, String> aliases,
                                                    final boolean processDeletes,
                                                    final long deleteProcessingDelay )
    {
        super( repository );
        this.aliases = Maps.newTreeMap();
        this.aliases.putAll( checkNotNull( aliases ) );
        this.processDeletes = processDeletes;
        this.deleteProcessingDelay = deleteProcessingDelay;
    }

    public GenerateMetadataCapabilityConfiguration( final Map<String, String> properties )
    {
        super( properties );

        this.aliases = Maps.newTreeMap();
        aliases.putAll( new AliasMappings( properties.get( ALIASES ) ).aliases() );

        boolean processDeletes = true;
        if ( properties.containsKey( DELETE_PROCESSING ) )
        {
            processDeletes = Boolean.parseBoolean( DELETE_PROCESSING );
        }
        this.processDeletes = processDeletes;

        long deleteProcessingDelay = Yum.DEFAULT_DELETE_PROCESSING_DELAY;
        try
        {
            deleteProcessingDelay = Long.parseLong( properties.get( DELETE_PROCESSING_DELAY ) );
        }
        catch ( NumberFormatException e )
        {
            // will use default
        }
        this.deleteProcessingDelay = deleteProcessingDelay;
    }

    public Map<String, String> aliases()
    {
        return aliases;
    }

    public long deleteProcessingDelay()
    {
        return deleteProcessingDelay;
    }

    public boolean shouldProcessDeletes()
    {
        return processDeletes;
    }

    public Map<String, String> asMap()
    {
        final Map<String, String> props = super.asMap();
        props.put( ALIASES, new AliasMappings( aliases ).toString() );
        props.put( DELETE_PROCESSING, String.valueOf( processDeletes ) );
        props.put( DELETE_PROCESSING_DELAY, String.valueOf( deleteProcessingDelay ) );
        return props;
    }

}
