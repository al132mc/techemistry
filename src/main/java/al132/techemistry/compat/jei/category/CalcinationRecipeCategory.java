package al132.techemistry.compat.jei.category;

import al132.techemistry.Ref;
import al132.techemistry.Techemistry;
import al132.techemistry.blocks.calcination_chamber.CalcinationRecipe;
import al132.techemistry.capabilities.heat.HeatHelper;
import al132.techemistry.compat.jei.JEIIntegration;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

import static al132.techemistry.capabilities.heat.HeatHelper.TempType.KELVIN;

public class CalcinationRecipeCategory implements IRecipeCategory<CalcinationRecipe> {
    private IGuiHelper guiHelper;
    private final static int u = 35;
    private final static int v = 10;

    public CalcinationRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public ResourceLocation getUid() {
        return JEIIntegration.CALCINATION_RESOURCE;
    }

    @Override
    public Class<? extends CalcinationRecipe> getRecipeClass() {
        return CalcinationRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("block.techemistry.calcination_chamber");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(new ResourceLocation(Techemistry.data.MODID, "textures/gui/calcination_gui.png"),
                u, v, 111, 76);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableIngredient(new ItemStack(Ref.calcinationChamber));
    }

    @Override
    public void setIngredients(CalcinationRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        List<ItemStack> outputs = Lists.newArrayList(recipe.getRecipeOutput());
        if (!recipe.getRecipeGas().isEmpty()) outputs.add(recipe.getRecipeGas().copy());
        if (!recipe.getRecipeOutput2().isEmpty()) outputs.add(recipe.getRecipeOutput2().copy());
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void draw(CalcinationRecipe recipe, MatrixStack ms, double mouseX, double mouseY) {
        Minecraft.getInstance().fontRenderer.drawString(ms, "Minimum Heat: " + HeatHelper.format(recipe.minimumHeat, KELVIN),
                46 - u, 70, Ref.TEXT_COLOR);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CalcinationRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 1) {
                tooltip.add(new StringTextComponent("Capture this gas by placing a Gas Collector above the Calcination Chamber"));
            }
        });
        int x = 43 - u;
        int y = 50 - v;
        guiItemStacks.init(0, true, x, y);
        guiItemStacks.set(0, Lists.newArrayList(recipe.getIngredients().get(0).getMatchingStacks()));

        if (!recipe.getRecipeGas().isEmpty()) {
            x = 82 - u;
            y = 12 - v;
            guiItemStacks.init(1, false, x, y);
            guiItemStacks.set(1, recipe.getRecipeGas().copy());
        }

        x = 123 - u;
        y = 41 - v;
        guiItemStacks.init(2, false, x, y);
        guiItemStacks.set(2, recipe.getRecipeOutput().copy());

        if (!recipe.getRecipeOutput2().isEmpty()) {
            x = 123 - u;
            y = 59 - v;
            guiItemStacks.init(3, false, x, y);
            guiItemStacks.set(3, recipe.getRecipeOutput2().copy());
        }
    }
}