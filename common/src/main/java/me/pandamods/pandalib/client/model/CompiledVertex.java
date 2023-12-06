package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.resources.MeshRecord;
import org.joml.Vector3f;

public record CompiledVertex(
		Vector3f position,
		MeshRecord.Object.Vertex data) {
}
