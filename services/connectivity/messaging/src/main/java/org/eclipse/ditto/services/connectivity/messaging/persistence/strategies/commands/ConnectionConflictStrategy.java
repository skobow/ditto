/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.connectivity.messaging.persistence.strategies.commands;

import static org.eclipse.ditto.services.utils.persistentactors.results.ResultFactory.newErrorResult;

import javax.annotation.Nullable;

import org.eclipse.ditto.model.connectivity.Connection;
import org.eclipse.ditto.services.connectivity.messaging.persistence.stages.ConnectionState;
import org.eclipse.ditto.services.utils.persistentactors.results.Result;
import org.eclipse.ditto.signals.commands.connectivity.exceptions.ConnectionConflictException;
import org.eclipse.ditto.signals.commands.connectivity.modify.CreateConnection;
import org.eclipse.ditto.signals.events.connectivity.ConnectivityEvent;

/**
 * This strategy handles the {@link org.eclipse.ditto.signals.commands.connectivity.modify.CreateConnection} command
 * when a conflict was encountered.
 */
final class ConnectionConflictStrategy extends AbstractConnectivityCommandStrategy<CreateConnection> {

    ConnectionConflictStrategy() {
        super(CreateConnection.class);
    }

    @Override
    protected Result<ConnectivityEvent> doApply(final Context<ConnectionState> context,
            @Nullable final Connection entity, final long nextRevision, final CreateConnection command) {
        context.getLog().info("Connection <{}> already exists! Responding with conflict.", context.getState().id());
        final ConnectionConflictException conflictException =
                ConnectionConflictException.newBuilder(context.getState().id())
                        .dittoHeaders(command.getDittoHeaders())
                        .build();
        return newErrorResult(conflictException);
    }
}
