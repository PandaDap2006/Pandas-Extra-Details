package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;

import java.util.ArrayList;
import java.util.List;

public interface CompileResultsExtension {
	default List<ClientBlockEntity> getClientBlockEntities() {
		return new ArrayList<>();
	}
}