# Как работать с проектом через Git Sparse Checkout

## Как скачать нужный тип проекта

1. Клонируем репозиторий без загрузки файлов:

```
git clone --no-checkout https://github.com/LeonardoDaVinciCreator/t-health.git
cd t-health
```

2. Инициализируем Sparse Checkout:

```
git sparse-checkout init --cone
```

3. Указываем нужный тип проекта (папку), рассмотрим на примере `mobile`:

```
git sparse-checkout set mobile
```

4. Для смены типа проекта:

```
git sparse-checkout set <название_папки>
```

5. Переключаемся на нужную ветку:

```
git checkout master
```

## Как вносить изменения и пушить

Работаем с Git как обычно:

```
git add .
git commit -m "Описание изменений"
git push origin master
```

> Обратите внимание: sparse checkout влияет только на локальную видимость файлов, пушит и коммитит Git всю ветку целиком.
