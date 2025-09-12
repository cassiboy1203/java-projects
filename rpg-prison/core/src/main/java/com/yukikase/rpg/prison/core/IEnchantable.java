package com.yukikase.rpg.prison.core;

import java.util.Map;

public interface IEnchantable {
    Map<IEnchantment, Integer> getEnchantments();

    IEnchantable setEnchantments(Map<IEnchantment, Integer> enchantments);

    IEnchantable addEnchantment(IEnchantment enchantment, int level);

    IEnchantable removeEnchantment(IEnchantment enchantment);

    IEnchantable removeEnchantments();
}
