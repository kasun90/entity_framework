package com.ust.spi;

import com.ust.spi.utils.SpiUtils;

/**
 * This is a generic response message which can be used to respond for a command.
 */
public final class CommandStatus {
    private final boolean success;
    private final String description;

    private CommandStatus(boolean success, String description) {
        this.success = success;
        this.description = description;
    }

    /**
     * Gets the status of the command execution.
     * @return the status
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the description of the command execution.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Static function to create success response.
     * @return the Command Status
     */
    public static CommandStatus succeeded() {
        return new CommandStatus(true, "SUCCESS");
    }

    /**
     * Static function to create success response.
     *
     * @param description the description of the command execution.
     * @return the Command Status
     */
    public static CommandStatus succeeded(String description) {
        return new CommandStatus(true, description);
    }

    /**
     * Static function to create failure response.
     *
     * @param description the description of the command execution.
     * @return the Command Status
     */
    public static CommandStatus failed(String description) {
        return new CommandStatus(false, description);
    }

    @Override
    public String toString() {
        return SpiUtils.getToString(this);
    }
}
