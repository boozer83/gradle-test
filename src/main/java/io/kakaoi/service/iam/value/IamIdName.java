package io.kakaoi.service.iam.value;

import java.util.Objects;

/**
 * Id, Name 값을 가지는 인터페이스
 */
public interface IamIdName {

    /**
     * IdAndName 인터페이스 내 id, name 값이 동일한지 체크한다.
     * @param left left
     * @param right right
     * @return left, right 객체가 동일하거나 id, name 내용물 동등 체크 결과
     */
    static boolean equalsValue(IamIdName left, IamIdName right) {
        if (left == right) return true;
        if (left == null || right == null) return false;
        return Objects.equals(left.getId(), right.getId()) && Objects.equals(left.getName(), right.getName());
    }

    String getId();

    String getName();

}
