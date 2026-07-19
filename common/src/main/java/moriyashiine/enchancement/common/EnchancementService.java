package moriyashiine.enchancement.common;

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

public interface EnchancementService {
	EnchancementService INSTANCE = ServiceLoader.load(EnchancementService.class, EnchancementService.class.getClassLoader()).findFirst().orElseThrow(() -> new NoSuchElementException("Unable to load %s service!".formatted(EnchancementService.class.getName())));

	void initAppleSkinIntegration();
}
