package com.wqb.commons.dto;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author Shoven
 * @since 2019-03-18 16:43
 */
public class MenuTree<T, R extends MenuNode> implements Serializable {

    private List<T> data = new ArrayList<>();

    /**
     * 父节点选择器
     */
    private Predicate<T> parentSelector;

    /**
     * 子节点选择器
     */
    private BiPredicate<T, T> childSelector;

    /**
     * 查找孩子停止条件（如果有）
     */
    private Predicate<T> stopCondition;

    /**
     * 对象转换器
     */
    private BiFunction<T, R, R> nodeMapper = (t1, t2) -> (R) t1;

    public MenuTree() {
    }

    public MenuTree(List<T> data) {
        this.data = data;
    }

    /**
     * 生成树结构列表
     *
     * @return
     */
    public List<R> generate() {
        if (data == null || data.isEmpty()) {
            return null;
        }

        checkNotNull(parentSelector, "父节点选择器不能为null");
        checkNotNull(childSelector, "孩子节点选择器不能为null");

        List<T> parents = getParents(data, parentSelector);
        List<R> newParents = new ArrayList<>();

        parents.forEach(oldParent -> {
            R newParent = nodeMapper.apply(oldParent, null);

            newParent.setChildren(findChildren(oldParent, newParent, data));
            newParents.add(newParent);
        });

        return newParents;
    }

    /**
     * 获取父节点列表
     *
     * @param nodes
     * @param parentSelector
     * @return
     */
    public List<T> getParents(List<T> nodes, Predicate<T> parentSelector) {
        return nodes.stream()
                .filter(current -> current != null && parentSelector.test(current))
                .collect(toList());
    }


    /**
     * 寻找孩子节点列表
     *
     * @param oldParent
     * @param newParent
     * @param nodes
     * @return
     */
    public List<R> findChildren(T oldParent, R newParent, List<T> nodes) {
        if (stopCondition != null && stopCondition.test(oldParent)) {
            return Collections.emptyList();
        }

        List<R> children = new ArrayList<>();

        // 继续寻找当前节点的所有子节点
        nodes.stream()
                .filter(node -> childSelector.test(oldParent, node))
                .forEach(oldNode -> {
                    R newNode = nodeMapper.apply(oldNode, newParent);
                    newNode.setChildren(findChildren(oldNode, newNode, nodes));
                    children.add(newNode);
                });

        return children;
    }

    /**
     * 寻找当前树里的节点
     *
     * @param nodes
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T extends MenuNode> T findTreeNode(List<T> nodes, Predicate<T> predicate) {
        T foundNode = null;
        for (T node : nodes) {
            if ((foundNode = findNode(node, predicate)) != null) {
                break;
            }
        }
        return foundNode;
    }

    /**
     * 寻找节点
     *
     * @param node
     * @param predicate
     * @param <T>
     * @return
     */
    private static <T extends MenuNode> T findNode(T node, Predicate<T> predicate) {
        if (predicate.test(node)) {
            return node;
        }

        List<T> children = (List<T>) node.getChildren();
        if (CollectionUtils.isNotEmpty(node.getChildren())) {
            return children.stream()
                    .map(child -> findNode(child, predicate))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public List<T> getData() {
        return data;
    }

    public MenuTree<T, R> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public Predicate<T> getParentSelector() {
        return parentSelector;
    }

    public MenuTree<T, R> setParentSelector(Predicate<T> parentSelector) {
        this.parentSelector = parentSelector;
        return this;
    }

    public BiPredicate<T, T> getChildSelector() {
        return childSelector;
    }

    public MenuTree<T, R> setChildSelector(BiPredicate<T, T> childSelector) {
        this.childSelector = childSelector;
        return this;
    }

    public Predicate<T> getStopCondition() {
        return stopCondition;
    }

    public MenuTree<T, R> setStopCondition(Predicate<T> stopCondition) {
        this.stopCondition = stopCondition;
        return this;
    }

    public BiFunction<T, R, R> getNodeMapper() {
        return nodeMapper;
    }

    public MenuTree<T, R> setNodeMapper(BiFunction<T, R, R> nodeMapper) {
        this.nodeMapper = nodeMapper;
        return this;
    }
}

