export class InitiateNullificationModal {
  constructor({ onSubmit }) {
    this.onSubmit = onSubmit;
    this.root = null;
    this.previousActiveElement = null;
  }

  open({ hireProcessId = "" } = {}) {
    this.close();
    this.previousActiveElement = document.activeElement;

    this.root = document.createElement("div");
    this.root.className = "modal-overlay";
    this.root.innerHTML = `
      <section class="modal" role="dialog" aria-modal="true" aria-labelledby="nullification-modal-title">
        <header class="modal__header">
          <div>
            <p class="modal__eyebrow">Кадровый процесс</p>
            <h2 id="nullification-modal-title">Инициировать аннулирование найма</h2>
          </div>
          <button class="icon-button" type="button" data-action="close" aria-label="Закрыть модальное окно">×</button>
        </header>

        <form class="modal__content" novalidate>
          <p class="modal__description">После инициации будет создан процесс аннулирования найма. Проверьте данные перед отправкой.</p>

          <div class="form-field">
            <label for="hire-process-id">ID процесса найма <span aria-hidden="true">*</span></label>
            <input id="hire-process-id" name="hireProcessId" type="text" value="${this.escapeHtml(hireProcessId)}" placeholder="Например, HIRE-2026-001245" autocomplete="off" required />
            <span class="field-error" data-error-for="hireProcessId" aria-live="polite"></span>
          </div>

          <div class="form-field">
            <label for="reason">Причина аннулирования <span aria-hidden="true">*</span></label>
            <select id="reason" name="reason" required>
              <option value="">Выберите причину</option>
              <option value="CANDIDATE_WITHDREW">Кандидат отозвал согласие</option>
              <option value="DUPLICATE_HIRE">Дублирующий процесс найма</option>
              <option value="DATA_ERROR">Ошибка в кадровых данных</option>
              <option value="BUSINESS_CANCELLATION">Отмена по решению бизнеса</option>
              <option value="OTHER">Другая причина</option>
            </select>
            <span class="field-error" data-error-for="reason" aria-live="polite"></span>
          </div>

          <div class="form-field">
            <label for="comment">Комментарий</label>
            <textarea id="comment" name="comment" rows="4" maxlength="500" placeholder="Укажите детали, если это необходимо"></textarea>
            <span class="form-hint">Не более 500 символов</span>
          </div>

          <label class="confirmation">
            <input id="confirmed" name="confirmed" type="checkbox" />
            <span>Я подтверждаю, что операция может повлиять на дальнейшее кадровое оформление сотрудника</span>
          </label>
          <span class="field-error" data-error-for="confirmed" aria-live="polite"></span>

          <footer class="modal__footer">
            <button class="button button--secondary" type="button" data-action="close">Отмена</button>
            <button class="button button--danger" type="submit">Инициировать</button>
          </footer>
        </form>
      </section>
    `;

    document.body.append(this.root);
    document.body.classList.add("modal-open");
    this.bindEvents();
    this.root.querySelector("#hire-process-id").focus();
  }

  bindEvents() {
    this.root.addEventListener("click", (event) => {
      if (event.target === this.root || event.target.closest('[data-action="close"]')) {
        this.close();
      }
    });

    this.onKeydown = (event) => {
      if (event.key === "Escape") {
        this.close();
      }
    };
    document.addEventListener("keydown", this.onKeydown);

    this.root.querySelector("form").addEventListener("submit", async (event) => {
      event.preventDefault();
      const formData = new FormData(event.currentTarget);
      const payload = {
        hireProcessId: String(formData.get("hireProcessId") || "").trim(),
        reason: String(formData.get("reason") || ""),
        comment: String(formData.get("comment") || "").trim() || null,
        confirmed: formData.get("confirmed") === "on",
      };

      if (!this.validate(payload)) {
        return;
      }

      const submitButton = event.currentTarget.querySelector('[type="submit"]');
      submitButton.disabled = true;
      submitButton.textContent = "Создаём процесс…";

      try {
        await this.onSubmit(payload);
        this.close();
      } catch (error) {
        this.showFormError(error?.message || "Не удалось инициировать аннулирование. Повторите попытку.");
      } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Инициировать";
      }
    });
  }

  validate(payload) {
    this.clearErrors();
    let isValid = true;

    if (!payload.hireProcessId) {
      this.showFieldError("hireProcessId", "Укажите ID процесса найма");
      isValid = false;
    }

    if (!payload.reason) {
      this.showFieldError("reason", "Выберите причину аннулирования");
      isValid = false;
    }

    if (!payload.confirmed) {
      this.showFieldError("confirmed", "Подтвердите выполнение операции");
      isValid = false;
    }

    return isValid;
  }

  showFieldError(fieldName, message) {
    const element = this.root.querySelector(`[data-error-for="${fieldName}"]`);
    if (element) {
      element.textContent = message;
    }
  }

  showFormError(message) {
    let element = this.root.querySelector(".form-error");
    if (!element) {
      element = document.createElement("div");
      element.className = "form-error";
      element.setAttribute("role", "alert");
      this.root.querySelector(".modal__content").prepend(element);
    }
    element.textContent = message;
  }

  clearErrors() {
    this.root.querySelectorAll(".field-error").forEach((element) => {
      element.textContent = "";
    });
    this.root.querySelector(".form-error")?.remove();
  }

  close() {
    if (!this.root) {
      return;
    }

    document.removeEventListener("keydown", this.onKeydown);
    this.root.remove();
    this.root = null;
    document.body.classList.remove("modal-open");
    this.previousActiveElement?.focus?.();
    this.previousActiveElement = null;
  }

  escapeHtml(value) {
    return String(value)
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }
}
